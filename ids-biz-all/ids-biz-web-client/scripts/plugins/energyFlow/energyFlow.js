(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define(function () {
            return factory(root);
        });
    } else if (typeof exports === 'object') {
        module.exports = factory;
    } else {
        root.energyFlow = factory(root);
    }
})(this, function (root) {
    'use strict';
    (function () {
        window.requestAnimationFrame = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame;
        if (!window.requestAnimationFrame) {
            window.requestAnimationFrame = function (callback) {
                var id = window.setTimeout(callback, 1000 / 60);
                return id;
            };
        }
        if (!window.cancelAnimationFrame) {
            window.cancelAnimationFrame = function (id) {
                clearTimeout(id);
            };
        }
    }());

    window.ballsMap = {};
    window.iconsMap = {};
    window.animation = null;

    function autoLayout(canvas, config) {
        var rowCounts = 1;
        var colCounts = 1;
        $.each(config.nodes, function (idx, node) {
            rowCounts = (node.row >= rowCounts) ? node.row : rowCounts;
            colCounts = (node.col >= colCounts) ? node.col : colCounts;
        });
        var rowHeight = (config.layout.height - 5) / rowCounts;
        var colWidth = config.layout.width / colCounts;

        $.each(config.nodes, function (idx, node) {
            var x = (node.col - 1) * colWidth;
            var y = (node.row - 1) * rowHeight;
            $.extend(node, {
                x: x,
                y: y,
                height: rowHeight,
                width: colWidth,
                iconHeight: node.iconHeight || rowHeight,
                iconWidth: node.iconWidth || colWidth,
                focused: false,
                description: transDescription(node.description)
            });
        });


        $.each(config.links, function (i, link) {
            $.extend(link, {
                description: transDescription(link.description)
            });

            var routings = [];
            var fromNode = $.grep(config.nodes, function (node) {
                return node.id == link.fromNode;
            })[0];
            var toNode = $.grep(config.nodes, function (node) {
                return node.id == link.toNode;
            })[0];
            var sPoint = getConnectorPoint(fromNode, link.fromConnector);
            var ePoint = getConnectorPoint(toNode, link.toConnector);
            routings.push(sPoint);

            if (fromNode.row != toNode.row) {
                var celWidth = fromNode.width;
                if (link.fromConnector.substr(0, 1) == "R" && link.toConnector.substr(0, 1) == "L" && true == link.merge) {
                    routings.push({x: ePoint.x - celWidth / 15, y: sPoint.y});
                    routings.push({x: ePoint.x - celWidth / 15, y: sPoint.y + (ePoint.y - sPoint.y)});
                } else if (link.fromConnector.substr(0, 1) == "L" || link.fromConnector.substr(0, 1) == "R") {
                    routings.push({x: ePoint.x, y: sPoint.y});
                } else if (link.fromConnector.substr(0, 1) == "B" || link.fromConnector.substr(0, 1) == "L") {
                    ePoint.y = ePoint.y - 20;
                    routings.push({x: sPoint.x, y: ePoint.y});
                } else if (link.fromConnector.substr(0, 1) == "T" || link.fromConnector.substr(0, 1) == "L") {
                    ePoint.y = ePoint.y + 3;
                    routings.push({x: sPoint.x, y: ePoint.y});
                }
            }
            routings.push(ePoint);

            var path = document.createElementNS('http://www.w3.org/2000/svg', 'path');
            var pathRouting = "";
            var newPoints = [];
            var r = 5;

            //调整为圆角
            $.each(routings, function (i, p) {
                if (i == 0) {
                    p.type = "M";
                    newPoints.push(p);
                    pathRouting += " M " + p.x + " " + p.y + " ";
                    return true;
                }
                if (i == routings.length - 1) {
                    p.type = "L";
                    newPoints.push(p);
                    pathRouting += " L " + p.x + " " + p.y + " ";
                    return true;
                }
                //垂直转水平
                if (routings[i - 1].x == p.x) {
                    var direction = {
                        x: routings[i + 1].x - routings[i - 1].x,
                        y: routings[i + 1].y - routings[i - 1].y
                    };
                    var p1 = {x: p.x, y: direction.y >= 0 ? (p.y - r) : (p.y + r), type: "L"}
                    p.type = "Q";
                    var p2 = {x: direction.x >= 0 ? (p.x + r) : (p.x - r), y: p.y, type: ""}
                    newPoints.push(p1, p, p2);
                    pathRouting += " L " + p1.x + " " + p1.y + " ";
                    pathRouting += " Q " + p.x + " " + p.y + " " + p2.x + " " + p2.y + " ";
                    return true;
                }

                //水平转垂直
                if (routings[i - 1].y == p.y) {
                    var direction = {
                        x: routings[i + 1].x - routings[i - 1].x,
                        y: routings[i + 1].y - routings[i - 1].y
                    };
                    var p1 = {x: direction.x >= 0 ? (p.x - r) : (p.x + r), y: p.y, type: "L"}
                    p.type = "Q";
                    var p2 = {x: p.x, y: (direction.y >= 0 ? (p.y + r) : (p.y - r)), type: ""}
                    newPoints.push(p1, p, p2);
                    pathRouting += " L " + p1.x + " " + p1.y + " ";
                    pathRouting += " Q " + p.x + " " + p.y + " " + p2.x + " " + p2.y + " ";
                    return true;
                }
            });
            path.setAttribute('d', pathRouting);
            $.extend(link, {
                routings: routings,
                points: newPoints,
                path: path,
                len: 0
            });
        });
    }

    function bindMouseEvent(canvas, ctx, config, lineDashSupport) {
        $(canvas).unbind("mousemove").mousemove(function (e) {
            $(canvas).parent("div").attr("title", "");
            var x = e.offsetX || e.clientX - $(canvas).offset().left,
                y = e.offsetY || e.clientY - $(canvas).offset().top;
            $.each(config.links, function (id, link) {
                if (link.tooltip) {
                    if (link.tooltip.sPoint.x <= x && x <= link.tooltip.ePoint.x
                        && link.tooltip.sPoint.y <= y && y <= link.tooltip.ePoint.y) {
                        $(canvas).parent("div").attr("title", link.tooltip.tip);
                    }
                }
            });
            $.each(config.nodes, function (id, node) {
                if (node.tooltip) {
                    if (node.tooltip.sPoint.x <= x && x <= node.tooltip.ePoint.x
                        && node.tooltip.sPoint.y <= y && y <= node.tooltip.ePoint.y) {
                        $(canvas).parent("div").attr("title", node.tooltip.tip);
                    }
                }
            });


            $.each(config.nodes, function (id, node) {
                if (!node.click)//无点击事件,不做鼠标悬停事件
                    return;
                if (!node.isHover) {
                    if (isInRange(canvas, e, node)) {
                        node.isHover = true;
                        $(canvas).css("cursor", "pointer");
                    } else {
                        node.isHover = false;
                    }
                } else {
                    if (!isInRange(canvas, e, node)) {
                        node.isHover = false;
                        $(canvas).css("cursor", "default");
                    } else {
                        node.isHover = true;
                    }
                }
            });
        });
        $(canvas).unbind("click").click(function (e) {
            $.each(config.nodes, function (id, node) {
                if (isInRange(canvas, e, node)) {
                    if (node.click && typeof(node.click) == "function") {
                        $.each(config.nodes, function (i, v) {//清空选择状态
                            v.status = "";
                            drawBorder(ctx, v, lineDashSupport);
                        });
                        node.status = "selected";
                        drawBorder(ctx, node, lineDashSupport);
                        node.click(node.customAttr);
                    }
                }
            });
        });
    }

    function isInRange(canvas, e, node) {
        var x = e.offsetX || e.clientX - $(canvas).offset().left,
            y = e.offsetY || e.clientY - $(canvas).offset().top;
        if (x >= node.x && x <= node.x + node.width
            && y >= node.y && y <= node.y + node.height) {
            return true;
        }
        return false;
    }

    function measureText(ctx, node, label, value) {
        ctx.font = "normal normal " + (node.description.labelFontSize || "14px") + " Microsoft Yahei";
        var labelWidth = ctx.measureText($.trim(label) != "" ? (label + "...") : node.description.label).width;
        ctx.font = "normal normal " + (node.description.valueFontSize || "14px") + " Microsoft Yahei";
        var valueWidth = ctx.measureText(value || node.description.value).width;
        return {labelWidth: labelWidth, valueWidth: valueWidth, width: labelWidth + valueWidth};
    }

    function adjustNodeText(ctx, node, label, value) {
        var measure = measureText(ctx, node, label, value);
        var Y = node.y + (node.height - node.iconHeight) / 2 + node.iconHeight + 18 + node.description.offset.y;
        var labelX = node.x + node.width / 2 - measure.width / 2 + measure.labelWidth / 2 + node.description.offset.x;
        var valueX = labelX + measure.width / 2;
        if (((labelX - measure.labelWidth / 2) < 0 || measure.width > 1.5 * node.width) && (label || node.description.label).length > 2) {
            var label = label || node.description.label;
            label = label.substring(label, label.length - 1);
            return adjustNodeText(ctx, node, label, node.description.value);
        }
        node.tooltip = {
            sPoint: {x: labelX - measure.labelWidth / 2, y: Y - 20},
            ePoint: {x: labelX + measure.labelWidth / 2, y: Y - 2},
            tip: node.description.label
        };
        return {
            label: {
                x: labelX,
                y: Y,
                text: $.trim(label) != "" ? label + "..." : node.description.label,
                width: measure.labelWidth
            },
            value: {x: valueX, y: Y, text: node.description.value, width: measure.valueWidth}
        };
    }

    function drawNode(ctx, node, createIcon, lineDashSupport) {
        if (createIcon) {
            ctx.clearRect(node.x, node.y, node.width, node.height);
            if (window.iconsMap[node.icon] == null) {
                var img = new Image();
                img.src = node.icon;
                $(img).load(function () {
                    window.iconsMap[node.icon] = img;
                    ctx.drawImage(img,
                        node.x + (node.width - node.iconWidth) / 2,
                        node.y + (node.height - node.iconHeight) / 2,
                        node.iconWidth, node.iconHeight);
                });
            } else {
                ctx.drawImage(window.iconsMap[node.icon],
                    node.x + (node.width - node.iconWidth) / 2,
                    node.y + (node.height - node.iconHeight) / 2,
                    node.iconWidth, node.iconHeight);
            }
        }

        if (node.render && typeof(node.render) == "function") {
            node.render(ctx, node);
        }
        if (node.preDescription) {
            var description = node.description;
            var perDescription = node.preDescription;
            node.description = perDescription;
            node.perDescription = description;
            var displayText = adjustNodeText(ctx, node);
            ctx.clearRect(displayText.label.x - displayText.label.width / 2 - 5, displayText.label.y - 12, displayText.label.width + displayText.value.width + 10, 18);
            node.description = description;
            node.perDescription = perDescription;
        }
        drawBorder(ctx, node, lineDashSupport);

        var displayText = adjustNodeText(ctx, node);
        //填充文字
        ctx.globalAlpha = 1;
        ctx.textAlign = "center";
        ctx.font = "normal normal " + (node.description.labelFontSize || "14px") + " Microsoft Yahei";
        ctx.fillStyle = (node.description.labelColor || "#333333");
        ctx.fillText(displayText.label.text, displayText.label.x, displayText.label.y);
        ctx.textAlign = "center";
        ctx.font = "normal normal " + (node.description.valueFontSize || "14px") + " Microsoft Yahei";
        ctx.fillStyle = (node.description.valueColor || "#19CE96");
        ctx.fillText(displayText.value.text, displayText.value.x, displayText.value.y);

    }

    function drawBorder(ctx, node, lineDashSupport) {
        var borderColor = "#FFFFFF";
        if (node.status == "selected") {
            borderColor = "#FFA500";
        }
        var padding = 5;
        var x = node.x + (node.width - node.iconWidth) / 2 - padding;
        var y = node.y + (node.height - node.iconHeight) / 2 - padding;
        var width = node.iconWidth + padding * 2;
        var height = node.iconHeight + padding * 2;
        var radius = 10;
        ctx.beginPath();
        ctx.strokeStyle = borderColor;
        ctx.lineWidth = "3";
        if(lineDashSupport){
            ctx.setLineDash([6, 6]);//设置虚线
        }
        //画圆角矩形
        ctx.arc(x + radius, y + radius, radius, Math.PI, Math.PI * 3 / 2);
        ctx.lineTo(width - radius + x, y);
        ctx.arc(width - radius + x, radius + y, radius, Math.PI * 3 / 2, Math.PI * 2);
        ctx.lineTo(width + x, height + y - radius);
        ctx.arc(width - radius + x, height - radius + y, radius, 0, Math.PI * 1 / 2);
        ctx.lineTo(radius + x, height + y);
        ctx.arc(radius + x, height - radius + y, radius, Math.PI * 1 / 2, Math.PI);
        ctx.closePath();
        ctx.save();
        ctx.stroke();
        if(lineDashSupport){
            ctx.setLineDash([]);//清空虚线设置
        }
    }

    function getTextPosition(ctx, link) {
        var pIdx = -1, max_len = 0;
        for (var i = 1; i < link.points.length; i++) {
            //只取横向线段
            if (Math.abs(link.points[i].y - link.points[i - 1].y) != 0) {
                continue;
            }
            var tmp_len = Math.sqrt(Math.pow(Math.abs(link.points[i].x - link.points[i - 1].x), 2) + Math.pow(Math.abs(link.points[i].y - link.points[i - 1].y), 2));
            if (tmp_len > max_len) {
                max_len = tmp_len;
                pIdx = i;
            }
        }
        return {width: max_len, sPoint: link.points[pIdx - 1], ePoint: link.points[pIdx]};
    }

    function adjustLinkText(ctx, link, label, value) {
        var textPosition = getTextPosition(ctx, link);
        var measure = measureText(ctx, link, label, value);
        var Y = textPosition.sPoint.y - 8 + link.description.offset.y;
        var labelX = textPosition.sPoint.x + textPosition.width / 2 - measure.width / 2 + measure.labelWidth / 2 + link.description.offset.x;
        var valueX = labelX + measure.width / 2;
        if (((labelX - measure.labelWidth / 2) < 0 || measure.width > textPosition.width) && (label || link.description.label).length > 2) {
            var label = label || link.description.label;
            label = label.substring(label, label.length - 1);
            return adjustLinkText(ctx, link, label, link.description.value);
        }
        link.tooltip = {
            sPoint: {x: labelX - measure.labelWidth / 2, y: Y - 20},
            ePoint: {x: labelX + measure.labelWidth / 2, y: Y - 2},
            tip: link.description.label
        };
        return {
            label: {
                x: labelX,
                y: Y,
                text: $.trim(label) != "" ? label + "..." : link.description.label,
                width: measure.labelWidth
            },
            value: {x: valueX, y: Y, text: link.description.value, width: measure.valueWidth}
        };
    }

    function drawLinkText(ctx, link) {
        if (link.preDescription) {
            var paddingWidth = 5;
            var description = link.description;
            var perDescription = link.preDescription;
            link.description = perDescription;
            link.perDescription = description;
            var position = getTextPosition(ctx, link);
            if (position.sPoint.x > position.ePoint.x) {
                paddingWidth = -paddingWidth;
            }
            ctx.clearRect(position.sPoint.x + paddingWidth, position.sPoint.y - 20 + link.description.offset.y, position.ePoint.x - position.sPoint.x - 2 * paddingWidth, 18);
            link.description = description;
            link.perDescription = perDescription;
        }

        if (link.flowing == "WARNING") {
            var position = getTextPosition(ctx, link);
            var errorIcon = "images/main/singlePlant/energyFlow/communicationFailure.png";
            if (window.iconsMap[errorIcon] == null) {
                var img = new Image();
                img.src = errorIcon;
                $(img).load(function () {
                    window.iconsMap[errorIcon] = img;
                    ctx.drawImage(img,
                        (position.sPoint.x + position.ePoint.x) / 2 - 5 + link.description.offset.x,
                        position.sPoint.y - 20 + link.description.offset.y,
                        18, 18);
                });
            } else {
                ctx.drawImage(window.iconsMap[errorIcon],
                    (position.sPoint.x + position.ePoint.x) / 2 - 5 + link.description.offset.x,
                    position.sPoint.y - 20 + link.description.offset.y,
                    18, 18);
            }
            return;
        }
        var displayText = adjustLinkText(ctx, link);
        //填充文字
        ctx.globalAlpha = 1;
        ctx.textAlign = "center";
        ctx.font = "normal normal " + (link.description.labelFontSize || "14px") + " Microsoft Yahei";
        ctx.fillStyle = "#333333";
        ctx.fillText(displayText.label.text, displayText.label.x, displayText.label.y);
        ctx.textAlign = "center";
        ctx.font = "normal normal " + (link.description.valueFontSize || "14px") + " Microsoft Yahei";
        ctx.fillStyle = (link.arrowColor || "#19CE96");
        ctx.fillText(displayText.value.text, displayText.value.x, displayText.value.y);
    }

    function drawLine(ctx, link, width, color, dash) {
        ctx.save();
        if (dash) {
            ctx.setLineDash(dash);
        }
        ctx.beginPath();
        for (var i = 0; i < link.points.length; i++) {
            if (link.points[i].type == "M") {
                ctx.moveTo(link.points[i].x, link.points[i].y);
            } else if (link.points[i].type == "L") {
                ctx.lineTo(link.points[i].x, link.points[i].y);
            } else if (link.points[i].type == "Q") {
                ctx.quadraticCurveTo(link.points[i].x, link.points[i].y, link.points[i + 1].x, link.points[i + 1].y);
            }
        }
        ctx.lineWidth = width;
        ctx.strokeStyle = color;
        ctx.stroke();
        ctx.closePath();
        ctx.restore();
    }

    function drawLink(ctx, link, lineDashSupport, ignoreText) {
        var linkColor = link.linkColor || "#19CE88";
        drawLine(ctx, link, 6, "#FFFFFF");
        if(link.flowing =="WARNING"){
            drawLine(ctx, link, 1, linkColor);
        }else{
            drawLine(ctx, link, 2, linkColor, lineDashSupport ? [2, 4]:null);
        }
        if (!ignoreText) {
            drawLinkText(ctx, link);
        }
    }

    function drawArrow(ctx, link) {
        if (link.flowing == "NONE") {
            return;
        }
        if (link.flowing == "WARNING") {
            var theta = 25;
            var headlen = 6;
            var width = 1;
            var sPoint = link.points[link.points.length -2];
            var ePoint = link.points[link.points.length -1];
            var fromX = sPoint.x;
            var fromY = sPoint.y;
            var toX = ePoint.x;
            var toY = ePoint.y;
            // 计算各角度和对应的P2,P3坐标
            var angle = Math.atan2(fromY - toY, fromX - toX) * 180 / Math.PI,
                angle1 = (angle + theta) * Math.PI / 180,
                angle2 = (angle - theta) * Math.PI / 180,
                topX = headlen * Math.cos(angle1),
                topY = headlen * Math.sin(angle1),
                botX = headlen * Math.cos(angle2),
                botY = headlen * Math.sin(angle2);
            ctx.save();
            ctx.beginPath();
            var arrowX = fromX - topX,
                arrowY = fromY - topY;
            ctx.moveTo(arrowX, arrowY);
            ctx.moveTo(fromX, fromY);
            ctx.lineTo(toX, toY);
            arrowX = toX + topX;
            arrowY = toY + topY;
            ctx.moveTo(arrowX, arrowY);
            ctx.lineTo(toX, toY);
            arrowX = toX + botX;
            arrowY = toY + botY;
            ctx.lineTo(arrowX, arrowY);
            ctx.strokeStyle = link.linkColor || "#19CE88";
            ctx.lineWidth = width;
            ctx.stroke();
            ctx.restore();
            return;
        }

        var arrowColor = link.arrowColor || "#19CE88";
        var radius = 15;
        var cvsBall;
        if (window.ballsMap[arrowColor] != null) {
            cvsBall = window.ballsMap[arrowColor];
        } else {
            cvsBall = document.createElement('canvas');
            var ctxBall = cvsBall.getContext('2d');
            cvsBall.width = 100;
            cvsBall.height = 100;
            ctxBall.beginPath();
            ctxBall.arc(50, 50, 15, 0, Math.PI * 2);
            ctxBall.fillStyle = arrowColor;
            ctxBall.fill();
            window.ballsMap[arrowColor] = cvsBall;
        }

        if("REVERSE" == link.flowing){
            link.len -= 2;
            (link.len <= 4) && (link.len = link.path.getTotalLength());
            (link.path.getTotalLength() - link.len <= 4 ) && (link.len = link.path.getTotalLength() - 4);
        } else {
            link.len += 2;
            (link.len >= link.path.getTotalLength()) && (link.len = 4);
            (link.path.getTotalLength() - link.len <= 4 ) && (link.len = 4);
        }
        var current = link.path.getPointAtLength(link.len);
        ctx.save();
        ctx.globalAlpha = 0.5;
        ctx.drawImage(cvsBall, current.x - radius / 2, current.y - radius / 2, radius, radius);
        ctx.restore();
    }

    function getConnectorOffset(connetcor) {
        var offsetSetting = {
            "2": 15,
            "3": 15
        };
        var offset = 0;
        if (connetcor.substr(1, 1) == "1" && connetcor.substr(2, 1) == "1") {
            return {x: 0, y: 0}
        } else if (parseInt(connetcor.substr(1, 1)) % 2 == 0) {
            offset = (parseInt(connetcor.substr(2, 1)) - (parseInt(connetcor.substr(1, 1))) / 2 - 0.5) * offsetSetting[connetcor.substr(1, 1)];
        } else {
            offset = (parseInt(connetcor.substr(2, 1)) - (parseInt(connetcor.substr(1, 1)) + 1) / 2) * offsetSetting[connetcor.substr(1, 1)];
        }

        if (connetcor.substr(0, 1) == "T" || connetcor.substr(0, 1) == "B") {
            return {x: offset, y: 0}
        } else {
            return {x: 0, y: offset}
        }
    }

    function getConnectorPoint(node, connetcor) {
        var xPadding = 7;
        var yPadding = 7;
        var x = node.x + (node.width - node.iconWidth) / 2;
        var y = node.y + (node.height - node.iconHeight) / 2;

        switch (connetcor.substr(0, 1)) {
            case "T":
                x = x + node.iconWidth / 2;
                y = y - yPadding;
                break;
            case "B":
                x = x + node.iconWidth / 2;
                y = y + node.iconHeight;
                y = y + yPadding;
                break;
            case "R":
                x = x + node.iconWidth;
                y = y + node.iconHeight / 2;
                x = x + xPadding;
                break;
            case "L":
                y = y + node.iconHeight / 2;
                x = x - xPadding;
                break;
        }
        var offset = getConnectorOffset(connetcor);
        return {x: x + offset.x, y: y + offset.y};
    }

    function transDescription(description) {
        description = $.extend(true, {
            label: "",
            labelFontSize: "14px",
            value: "",
            valueFontSize: "14px",
            offset: {x: 0, y: 0}
        }, description);
        if ($.trim(description.label) != "") {
            if (description.label.indexOf("Msg") != -1) {
                description.label = eval("window." + description.label);//国际化
            }
        }
        if ($.trim(description.value) != "" && description.value.split("#").length == 2) {
            var temp = parseFloat(description.value.split("#")[0]);
            var negative = temp < 0;
            var display = convert(Math.abs(temp), main.Lang + '_' + main.region).from(description.value.split("#")[1]).toBest();
            description.value = " " +(negative == true ? "-":"")+ parseFloat(display.val).toFixed(2) + " " + display.unit;
        }
        return description;
    }

    function needReCreate(curConfig, preConfig){

        if((curConfig != null && preConfig == null)
            ||(curConfig == null && preConfig != null)){
            return true;
        }
        if(curConfig == null && preConfig == null){
            return false;
        }
        if(curConfig.nodes.length != preConfig.nodes.length){
            return true;
        }
        if(curConfig.links.length != preConfig.links.length){
            return true;
        }

        for(var i = 0 ; i<curConfig.nodes.length; i++){
            if(parseInt(curConfig.nodes[i].id) != parseInt(preConfig.nodes[i].id)){
                return true
            }
        }
        for(var i = 0 ; i<curConfig.links.length; i++){
            if(parseInt(curConfig.links[i].id) != parseInt(preConfig.links[i].id)){
                return true
            }
        }
        return false;
    }


    $.fn.EnergyFlow = function () {
        return new EnergyFlow($(this)[0]);
    };

    function EnergyFlow(canvas) {
        this.lineDashSupport = (!main.getBrowser().msie || parseFloat(main.getBrowser().version) >= 11);
        this.canvas = canvas;
        this.preConfig = null;
        this.curConfig = null;
        this.animationId = "";
        this.id = "EF#"+new Date().getTime();
        $(canvas).attr("EFID",this.id);
        this.init();
    }

    EnergyFlow.prototype = {
        bind: function(canvas){
            this.canvas = canvas;
            this.preConfig = null;
            this.curConfig = null;
            this.id = "EF#"+new Date().getTime();
            $(canvas).attr("EFID",this.id);
            this.init();
        },
        resize: function(canvas){
            var config = this.curConfig||this.preConfig;
            config.layout = {
                width: $(this.canvas).width(),
                height: $(this.canvas).height()
            };
            this.bind(canvas);
            this.render(config);
        },
        init: function () {
            if (!this.canvas.getContext) {
                window.G_vmlCanvasManager.initElement(this.canvas);
            }
            this.canvas.width = $(this.canvas).width();
            this.canvas.height = $(this.canvas).height();
            this.ctx = this.canvas.getContext("2d");
            var _this = this;
            cancelAnimationFrame(_this.animationId);
            var animate = function () {
                if (_this.curConfig != null) {
                    var createFlag = needReCreate(_this.curConfig, _this.preConfig);
                    var iconCreateFlag = {};
                    for (var i = 0; i < _this.curConfig.nodes.length; i++) {
                        iconCreateFlag[_this.curConfig.nodes[i].id] = createFlag;
                    }
                    if (createFlag) {
                        autoLayout(_this.canvas, _this.curConfig);
                        bindMouseEvent(_this.canvas, _this.ctx, _this.curConfig, _this.lineDashSupport);
                        _this.ctx.clearRect(0, 0, $(_this.canvas).width(), $(_this.canvas).height());
                    } else {
                        for (var i = 0; i < _this.curConfig.nodes.length; i++) {
                            iconCreateFlag[_this.curConfig.nodes[i].id] = (_this.curConfig.nodes[i].icon != _this.preConfig.nodes[i].icon);
                            $.extend(true, _this.curConfig.nodes[i], {
                                iconWidth: _this.preConfig.nodes[i].iconWidth,
                                iconHeight: _this.preConfig.nodes[i].iconHeight,
                                width: _this.preConfig.nodes[i].width,
                                height: _this.preConfig.nodes[i].height,
                                x: _this.preConfig.nodes[i].x,
                                y: _this.preConfig.nodes[i].y,
                                step: _this.preConfig.nodes[i].step,
                                render: _this.preConfig.nodes[i].render,
                                description: transDescription(_this.curConfig.nodes[i].description),
                                preDescription: _this.preConfig.nodes[i].description
                            });
                        }

                        for (var i = 0; i < _this.curConfig.links.length; i++) {
                            $.extend(true, _this.curConfig.links[i], {
                                routings: _this.preConfig.links[i].routings,
                                points: _this.preConfig.links[i].points,
                                path: _this.preConfig.links[i].path,
                                len: _this.preConfig.links[i].len,
                                description: transDescription(_this.curConfig.links[i].description),
                                preDescription: _this.preConfig.links[i].description
                            })
                        }
                    }
                    $.each(_this.curConfig.nodes, function (idx, node) {
                        drawNode(_this.ctx, node, iconCreateFlag[node.id] || _this.preConfig == null, _this.lineDashSupport);
                    });
                    $.each(_this.curConfig.links, function (idx, link) {
                        drawLink(_this.ctx, link, _this.lineDashSupport, false);
                    });
                    $.each(_this.curConfig.links, function (idx, link) {
                        drawArrow(_this.ctx, link);
                    });
                    _this.preConfig = _this.curConfig;
                } else if(_this.preConfig != null){
                    $.each(_this.preConfig.nodes, function (idx, node) {
                        if(node.render){
                            node.render(_this.ctx, node);
                        }
                    });
                    $.each(_this.preConfig.links, function (idx, link) {
                        drawLink(_this.ctx, link, _this.lineDashSupport, true);
                    });
                    $.each(_this.preConfig.links, function (idx, link) {
                        drawArrow(_this.ctx, link);
                    });
                }
                _this.curConfig = null;
                _this.animationId = requestAnimationFrame(animate);
            };
            animate();
        },
        render: function (config) {
            this.curConfig = config;
            if (!this.curConfig.layout) {
                this.curConfig.layout = {};
            }
            this.curConfig.layout = {
                width: this.curConfig.layout.width || $(this.canvas).width(),
                height: this.curConfig.layout.height || $(this.canvas).height()
            };
        }
    };
    return EnergyFlow;
});