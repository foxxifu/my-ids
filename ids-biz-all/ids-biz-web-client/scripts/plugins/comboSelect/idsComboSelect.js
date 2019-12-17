define(['jquery', 'css!plugins/comboSelect/combo.select.css', 'plugins/comboSelect/jquery.combo.select'], function ($) {
    if ($.fn.idsComboSelect) {
        return $;
    }

    $.fn.idsComboSelect = function (option) {
        var $this = $(this);
        var p = $.extend({
            inGridTable: false,
            inDialogForm: false
        }, option);

        var f = {
            _init: function () {
                $this.comboSelect();
                if (p.inGridTable) {
                    f._fitTable();
                }
                if (p.inDialogForm) {
                	f._fitDialog();
                }
            },
            _fitDialog: function(){
            	$this.hide();
            	$this.siblings("input").css("width",$this.css("width"));
            	$this.siblings('ul').hide();
                $this.siblings("input").on("focus", function () {
                    var that = this;
                    var ulTop = $(that).offset().top - $(that).parents(".modal-dialog").offset().top + $(that).parent().height();
                    

//                          var scrollTop =  $(that).parents('div.GridTableBodyDiv').parent().scrollTop();
//                          var trCount = $(that).parents("tbody").find("tr").length;
//                          var curIndex = $(that).parents("tr").index();
                    var screenDis = $(window).height() - $(that).offset().top - $(that).height();
                    if (!$(that).parent().hasClass('combo-open')) {
                        $(that).parent().addClass('combo-open');
                    }
                    if (!$(that).parent().hasClass('combo-focus')) {
                        $(that).parent().addClass('combo-focus');
                    }
                    $(that).siblings("ul").css({
                        "z-index": "20",
                        "width": $(that).css("width")
                    });
                    /*$(that).siblings("ul").css({
                            "position": "fixed",
                            "z-index": "20",
                            "top": ulTop + 'px',
                            "left": 'inherit',
                            "min-width": $(that).css("width"),
                            "bottom": ''
                        });*/
                    $(that).siblings('ul').width($(that).siblings('ul').width()+20);
                    $(that).siblings('ul').show();
                });
            },
            _fitTable: function () {
                $this.siblings('ul').hide();
                $this.siblings("input").on("focus", function () {
                    var that = this;
                    var ulTop = $(that).parent().offset().top + $(that).parent().height();
                    var ulLeft = $(that).parent().offset().left;

//                          var scrollTop =  $(that).parents('div.GridTableBodyDiv').parent().scrollTop();
//                          var trCount = $(that).parents("tbody").find("tr").length;
//                          var curIndex = $(that).parents("tr").index();
                    var screenDis = $(window).height() - $(that).offset().top - $(that).height();
                    if (!$(that).parent().hasClass('combo-open')) {
                        $(that).parent().addClass('combo-open');
                    }
                    if (!$(that).parent().hasClass('combo-focus')) {
                        $(that).parent().addClass('combo-focus');
                    }
                    if (screenDis < 300) {
                        $(that).siblings("ul").css({
                            "position": "fixed",
                            "left": ulLeft + 'px',
                            "z-index": "20",
                            "min-width": $(that).css("width"),
                            "bottom": $(window).height() - $(that).offset().top + 'px',
                            "top": "auto"
                        });
                    } else {
                        $(that).siblings("ul").css({
                            "position": "fixed",
                            "z-index": "20",
                            "top": ulTop + 'px',
                            "left": ulLeft + 'px',
                            "min-width": $(that).css("width"),
                            "bottom": ''
                        });
                    }
                    $(that).siblings('ul').show();
//                          var scrollT = $(that).parents('div.GridTableBodyDiv').scrollTop();

                    $(that).parents('div.GridTableBodyDiv').off('scroll').scroll(function () {
                        console.log('Combo select scroll');
                        // 方案1： 禁用掉外部父div的滚动
                        // $(this).prop('scrollTop', scrollT);
                        // return;
                        // 方案2： 外部父div滚动时隐藏掉下拉
                        $(that).trigger('blur');
                        $(that).parent().removeClass("combo-focus").removeClass("combo-open");
                        $(that).parents('div.GridTableBodyDiv').off('scroll');
                    });

                    $(that).parents('div.GridTableBodyDiv').on('resize', function () {
                        console.log('Combo select resize');
                        $(that).trigger('blur');
                        $(that).parent().removeClass("combo-focus").removeClass("combo-open");
                    });
                });
            }
        };

        f._init();
    };

    return $;
});