/**
 * Ajax 三级省市联动
 * settings 参数说明 ----- url:省市数据josn文件路径 prov:默认省份 city:默认城市 dist:默认地区（县）
 * nodata:无数据状态 required:必选项
 * 2017/01/17
 */
(function ($) {
    $.fn.citySelect = function (options) {
        if (this.length < 1) {
            return;
        }

        // 默认值
        options = $.extend({
            url: "/js/plugins/areaSelect/city.data.json",
            prov: null,
            city: null,
            dist: null,
            nodata: 'none',
            required: true
        }, options);
        var box_obj = this;
        var prov_obj = box_obj.find(".prov");
        var city_obj = box_obj.find(".city");
        var dist_obj = box_obj.find(".dist");

        if (prov_obj.length == 0) {
            prov_obj = $('<select>').attr('name', 'prov').addClass('prov');
            box_obj.append(prov_obj);
        }
        if (city_obj.length == 0) {
            city_obj = $('<select>').attr('name', 'city').addClass('city');
            box_obj.append(city_obj);
        }
        if (dist_obj.length == 0) {
            dist_obj = $('<select>').attr('name', 'dist').addClass('dist');
            box_obj.append(dist_obj);
        }

        var prov_val = options.prov;
        var city_val = options.city;
        var dist_val = options.dist;
        var select_prehtml = (options.required) ? "" : "<option value=''>请选择</option>";
        var city_json;

        // 赋值市级函数
        var cityStart = function () {
            var prov_id = prov_obj.get(0)[prov_obj.get(0).selectedIndex].dataset.code;
            if (!options.required) {
                prov_id = "";
            }
            city_obj.empty().attr("disabled", true);
            dist_obj.empty().attr("disabled", true);

            if (prov_id && typeof(city_json.city[prov_id]) == "undefined") {
                if (options.nodata == "none") {
                    city_obj.css("display", "none");
                    dist_obj.css("display", "none");
                } else if (options.nodata == "hidden") {
                    city_obj.css("visibility", "hidden");
                    dist_obj.css("visibility", "hidden");
                }
                return;
            }

            // 遍历赋值市级下拉列表
            var temp_html = select_prehtml;
            $.each(city_json.city[prov_id], function (i, city) {
                temp_html += "<option value='" + city.code + "' data-code='" + i + "'>" + city.name + "</option>";
            });
            city_obj.html(temp_html).attr("disabled", false).css({"display": "", "visibility": ""});
            distStart();
        };

        // 赋值地区（县）函数
        var distStart = function () {
            var prov_id = prov_obj.get(0)[prov_obj.get(0).selectedIndex].dataset.code;
            var city_id = city_obj.get(0)[city_obj.get(0).selectedIndex].dataset.code;
            if (!options.required) {
                prov_id = "";
                city_id = "";
            }
            dist_obj.empty().attr("disabled", true);

            if (prov_id && city_id && typeof(city_json.dist[city_id]) == "undefined") {
                if (options.nodata == "none") {
                    dist_obj.css("display", "none");
                } else if (options.nodata == "hidden") {
                    dist_obj.css("visibility", "hidden");
                }
                return;
            }

            // 遍历赋值市级下拉列表
            var temp_html = select_prehtml;
            $.each(city_json.dist[city_id], function (i, dist) {
                temp_html += "<option value='" + dist.code + "' data-code='" + i + "'>" + dist.name + "</option>";
            });
            dist_obj.html(temp_html).attr("disabled", false).css({"display": "", "visibility": ""});
        };

        var init = function () {
            // 遍历赋值省份下拉列表
            var temp_html = select_prehtml;
            $.each(city_json["prov"], function (i, prov) {
                temp_html += "<option value='" + prov.code + "' data-code='" + i + "'>" + prov.name + "</option>";
            });
            prov_obj.html(temp_html);

            // 若有传入省份与市级的值，则选中。（setTimeout为兼容IE6而设置）
            setTimeout(function () {
                if (options.prov != null) {
                    prov_obj.val(options.prov || '110000');
                    cityStart();
                    setTimeout(function () {
                        if (options.city != null) {
                            city_obj.val(options.city);
                            distStart();
                            setTimeout(function () {
                                if (options.dist != null) {
                                    dist_obj.val(options.dist);
                                }
                            }, 1);
                        }
                    }, 1);
                }
            }, 1);

            // 选择省份时发生事件
            prov_obj.bind("change", function () {
                cityStart();
            });

            // 选择市级时发生事件
            city_obj.bind("change", function () {
                distStart();
            });
        };

        // 设置省市json数据
        if (typeof(options.url) == "string") {
            $.getJSON(options.url, function (json) {
                city_json = json;
                init();
            });
        } else {
            city_json = options.url;
            init();
        }
    };
})(jQuery);