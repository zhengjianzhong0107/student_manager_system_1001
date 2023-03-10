/**
 *
 *option = {
	trData: null, //必传参数，工具条回调参数obj
	lay_event: null, //必传参数，工具条按钮事件名string
	childColor: "#FFFFFF", //选项颜色，可选
	childHoverColor: "#1E9FFF", //选项经过颜色，可选
	childHeight: 30, //选项高度，可选
	toolWidth: 200, //工具总宽度，可选
	space: 0, //孩子边缘间距
	event: [{ //插入点击选项，不填则为默认示例
		eventName: 'sele', //事件名称
		text: '查找', //显示文字
		insert: '', //在显示文字前插入，可插入dom
		callback: function(e) { //事件回调函数
			var data = e.data; //当前行数据
			console.log('e', e);
		}
	}]
}
 *
 *
 *
 */


layui.define(['layer'], function(exports) {
    var $ = layui.$,
        layer = layui.layer;
    var moretool = {
        render: function(params) {
            if (params.trData == null) {
                console.error("请传入工具条obj数据");
                return;
            } else if (params.lay_event == null) {
                console.error("请传入工具条lay-event数据");
                return;
            }
            if(params.childColor == null || params.childColor == undefined){
                params.childColor = "#FFFFFF";
            }

            if(params.childHoverColor == null || params.childHoverColor == undefined){
                params.childHoverColor = "#1E9FFF";
            }
            if(params.childHeight == null || params.childHeight == undefined){
                params.childHeight = 30;
            }
            if(params.toolWidth == null || params.toolWidth == undefined){
                params.toolWidth = 200;
            }
            if(params.space == null || params.space == undefined){
                params.space = 0;
            }

            if(params.selectTool == null || params.selectTool == undefined){
                params.selectTool =  [
                    '<div class="selectTool">',
                    // '<div class="child" data-event="sele">查找</div>',
                    // '<div class="child" data-event="add">添加</div>',
                    // '<div class="child" data-event="del">删除</div>',
                    // '<div class="child" data-event="check">审查</div>',
                    '</div>'
                ];
            }


            //生成插入页面
            for (var i = 0; i < params.event.length; i++) {
                params.event[i].insert = params.event[i].insert || '';
                params.selectTool.splice(1 + i, 0, '<div class="child" data-event="' + params.event[i].eventName + '">' + params.event[i].insert +
                    '<span style="user-select:none">'+ params.event[i].text + '</span></div>');
            }

            var childCount = params.selectTool.length - 2, //孩子个数
                childWidth = params.toolWidth,
                toolHeight = (params.childHeight * childCount) + ((childCount - 1) * space), //总高度
                data = params.trData.data, //行数据
                tr = params.trData.tr; //行dom
            var width = $(window).width();
            var height = $(window).height();
            var button = tr.find('a[lay-event="' + params.lay_event + '"]');
            var buttonPos = {
                width: button.width(),
                height: button.height()
            };
            var pos = button.offset(); //获得按钮位置

            var popPos = pos; //弹出位置
            var devX = width - pos.left; //偏差X
            var devY = height - pos.top; //偏差Y
            popPos.left += buttonPos.width / 2;
            popPos.top += buttonPos.height / 2;
            //判断弹出位置
            if (devX < params.toolWidth) {
                popPos.left -= params.toolWidth;
            }
            if (devY < toolHeight) {
                popPos.top -= toolHeight;
            }
            //监听事件
            var eventOn = function(layero, index) {
                layero.find('.child').each(function() {
                    var clickEvent = $(this).data('event');
                    //首先判断事件再监听,减少后续判断数量
                    for (var i = 0; i < params.event.length; i++) {

                        if (clickEvent == params.event[i].eventName) {
                            var num = i;
                            $(this).on('click', function() {
                                if (params.event[num].callback) {
                                    params.event[num].data = data;
                                    params.event[num].callback.call(this, params.event[num]);
                                }
                                layer.close(index);
                            });
                        }
                    }
                });
            };
            //弹出层
            var toolIndex = layer.open({
                id: 'selectTool',
                closeBtn: 0,
                isOutAnim: false,
                resize: false,
                title: false,
                type: 1,
                shade: [0.1, '#ffffff'],
                shadeClose: true,
                // anim: 1,
                content: params.selectTool.join(''),
                offset: [Number(popPos.top) + 'px', Number(popPos.left) + 'px'],
                success: function(layero, index) {
                    layero.find('.selectTool').css({
                        "background-color": "#ffffff",
                        "width": params.toolWidth + "px",
                        "height": toolHeight + "px"
                    });
                    layero.find('.child').each(function(i, item) {
                        $(this).css({
                            "background-color": params.childColor,
                            "width": childWidth + "px",
                            "height": params.childHeight + "px",
                            "margin-bottom": params.space + "px",
                            "text-align": "center",
                            "vertical-align": "middle",
                            "color": "#000000",
                            "line-height": params.childHeight + "px"
                        });
                    });
                    //事件监听
                    eventOn.call(this, layero, index);
                    //经过孩子样式
                    layero.find('.child').hover(function() {
                        $(this).css({
                            "background-color": params.childHoverColor,
                            "color": "#FFFFFF"
                        });
                    }, function() {
                        $(this).css({
                            "background-color": params.childColor,
                            "color": "#000000"
                        });
                    });
                }
            });
        }
    };
    exports('moretool', moretool);
});