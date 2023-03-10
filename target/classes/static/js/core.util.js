/*工具类*/
var CoreUtil = (function () {
    var coreUtil = {};

    /*GET*/
    coreUtil.sendGet = function(url, params, ft){
        this.sendAJAX(url, params, ft, "GET")
    }

    /*POST*/
    coreUtil.sendPost = function(url, params, ft, async){
        this.sendAJAX(url, JSON.stringify(params), ft, "POST", async)
    }
    /*PUT*/
    coreUtil.sendPut = function(url, params, ft){
        this.sendAJAX(url, JSON.stringify(params), ft, "PUT")
    }
    /*DELETE*/
    coreUtil.sendDelete = function(url, params, ft){
        this.sendAJAX(url, JSON.stringify(params), ft, "DELETE")
    }

    coreUtil.isRealNum = function(val){
        // isNaN()函数 把空串 空格 以及NUll 按照0来处理 所以先去除，
        if(val === "" || val ==null){
            return false;
        }
        if(!isNaN(val)){
            //对于空数组和只有一个数值成员的数组或全是数字组成的字符串，isNaN返回false，例如：'123'、[]、[2]、['123'],isNaN返回false,
            //所以如果不需要val包含这些特殊情况，则这个判断改写为if(!isNaN(val) && typeof val === 'number' )
            return true;
        }
        else{
            return false;
        }
    };


    /*ajax*/
    coreUtil.sendAJAX = function(url, params, ft, method, async){
        var loadIndex = top.layer.load(0, {shade: false});
        if(async == null || async == undefined){
            async = true;
        }
        $.ajax({
            url: url,
            cache: false,
            async: async,
            data: params,
            type: method,
            contentType: 'application/json; charset=UTF-8',
            dataType: "json",
            beforeSend: function(request) {
                request.setRequestHeader("authorization", CoreUtil.getData("access_token"));
            },
            success: function (res) {
                top.layer.close(loadIndex);
                if (res.code==0){
                    if(ft!=null&&ft!=undefined){
                        ft(res);
                    }
                }else if(res.code==401001){ //凭证过期重新登录
                    layer.msg("凭证过期请重新登录", {time:2000}, function () {
                        top.window.location.href="/index/login"
                    })
                }else if(res.code==401008){ //凭证过期重新登录
                    layer.msg("抱歉！您暂无权限", {time:2000})
                } else {
                    layer.msg(res.msg);
                }
            },
            error:function (XMLHttpRequest, textStatus, errorThrown) {
                top.layer.close(loadIndex);
                if(XMLHttpRequest.status==404){
                    top.window.location.href="/index/404";
                }else{
                    layer.msg("服务器好像除了点问题！请稍后试试");
                }
            }
        })
    }


    /*存入本地缓存*/
    coreUtil.setData = function(key, value){
        layui.data('LocalData',{
            key :key,
            value: value
        })
    };
    /*从本地缓存拿数据*/
    coreUtil.getData = function(key){
        var localData = layui.data('LocalData');
        return localData[key];
    };

    //判断字符是否为空的方法
    coreUtil.isEmpty = function(obj){
        if(typeof obj == "undefined" || obj == null || obj == ""){
            return true;
        }else{
            return false;
        }
    }

    //字典数据回显
    coreUtil.selectDictLabel = function (datas, value) {
        datas = JSON.parse(datas);
        var label = "";
        $.each(datas, function(index, dict) {
            if (dict.value == ('' + value)) {
                label = dict.label;
                return false;
            }
        });
        //匹配不到，返回未知
        if (CoreUtil.isEmpty(label)) {
            return "未知";
        }
        return label;
    };


    coreUtil.strReplace = function (str, oldPart, newPart){
        var reg = new RegExp(oldPart,"g");
        return str.replace(reg, newPart);
    }

    coreUtil.replace = function (str, oldPart, newPart){
        return str.replace(oldPart, newPart);
    }

    coreUtil.courseNameFormat = function (name){
        var arr = name.split(/\s{2,}/);
        var html = "";
        var length = arr.length;
        if(length > 1){
            html += "<b>" + arr[0] + "</b><br/>";
            for(var i = 1; i < length; i++){
                html += arr[i] + "<br/>";
            }
        }else{
            return arr[0];
        }
        return html;
    };

    /**
     * 课表表格格式化，对文本进行格式化，对不同的科目进行格式化背景颜色---未开发
     * @param label 显示文本
     * @param node 表格格子节点对象
     */
    coreUtil.tableTdFormat = function (label, node){

    }


    /**
     * 阿拉伯数字转中文数字
     * @param num
     * @returns {string}
     * @constructor
     */
    coreUtil.NoToChinese = function (num) {
        if (!/^\d*(\.\d*)?$/.test(num)) {
            alert("Number is wrong!");
            return "";
        }
        var AA = new Array("零", "一", "二", "三", "四", "五", "六", "七", "八", "九");
        var BB = new Array("", "十", "百", "千", "万", "亿", "点", "");
        var a = ("" + num).replace(/(^0*)/g, "").split("."),
            k = 0,
            re = "";
        for (var i = a[0].length - 1; i >= 0; i--) {
            switch (k) {
                case 0:
                    re = BB[7] + re;
                    break;
                case 4:
                    if (!new RegExp("0{4}\\d{" + (a[0].length - i - 1) + "}$").test(a[0]))
                        re = BB[4] + re;
                    break;
                case 8:
                    re = BB[5] + re;
                    BB[7] = BB[5];
                    k = 0;
                    break;
            }
            if (k % 4 == 2 && a[0].charAt(i + 2) != 0 && a[0].charAt(i + 1) == 0) re = AA[0] + re;
            if (a[0].charAt(i) != 0) re = AA[a[0].charAt(i)] + BB[k % 4] + re;
            k++;
        }
        if (a.length > 1) //加上小数部分(如果有小数部分)
        {
            re += BB[6];
            for (var i = 0; i < a[1].length; i++) re += AA[a[1].charAt(i)];
        }
        return re;
    };



    //教师信息回显
    /**
     * 获取对应的文本描述
     * @param datas 数据list
     * @param value 需要查找的元素的关键值
     * @param key 需要匹配的属性名称
     * @param value_key_one 文本显示字段1
     * @param value_key_two 文本显示字段2
     * @returns {string}
     */
    coreUtil.getBeanLabel = function (datas, value, key, value_key_one, value_key_two) {
        datas = JSON.parse(datas);
        var name = "";
        $.each(datas, function(index, bean) {
            if (bean[key] == ('' + value)) {
                if(value_key_one != null && value_key_one != undefined){
                    name = bean[value_key_one];
                }else{
                    name = bean.label;
                }
                if(value_key_two != null && value_key_two != undefined){
                    name = name + "(" + bean[value_key_two] + ")";
                }
                return false;
            }
        });
        //匹配不到，返回未知
        if (CoreUtil.isEmpty(name)) {
            return "未知";
        }
        return name;
    };


    /**
     *
     * @param url
     * @param params
     * @param async
     */
    coreUtil.getDataByUrl = function(url, params){

        var data = null;
        coreUtil.sendPost(url, params, function (res) {
            data = res.data;
        }, false);
        return data;
    }




    coreUtil.getClassLabel = function (datas, value) {
        var name = "";
        datas = JSON.parse(datas);
        $.each(datas, function(index, bean) {

            if (bean.id == value) {
                name = bean.grade.gradeNameLabel + " " + bean.classNo + "班";
                return false;
            }
        });
        //匹配不到，返回未知
        if (CoreUtil.isEmpty(name)) {
            return "未知";
        }
        return name;
    };

    /**
     * 通过属性值从集合中取到对应该属性值的数据对象中的指定属性的值
     * @param datas 集合
     * @param key_value 已知属性的值
     * @param key_name 已知属性的名称
     * @param value_name 需要获取的属性名称
     * @returns {null}
     */
    coreUtil.findValueByKey =  function(datas , key_value , key_name, value_name){
        datas = JSON.parse(datas);
        $.each(datas, function(index, bean) {
            if(bean[key_name] == key_value){
                return bean[value_name];
            }
        });
        return null;
    };
    /**
     * 获取简略的字符串信息
     * @param str 原字符串
     * @param length 获取的长度
     */
    coreUtil.getBriefOfString = function (str, length) {
        //如果截取的长度大于等于了本来该字符串的长度，就直接返回
        if(length >= str.length){
            return str;
        }
        return str.substring(0, length) + "...";
    }

    /**
     * 将数据解析成为分组下拉框的数据
     * @param data
     * @param parentKeyName 判断分组的属性名称
     * @param valueKey option中需要放在value处的属性名
     * @param labelKey option中需要放在label处的属性名
     * @param groupKeyValue 分组的名称与对应的key值
     * @param node 需要添加的节点
     */
    coreUtil.getSelectGroupData = function (data, parentKeyName, valueKey, labelKey, groupKeyValue, node) {
        data = JSON.parse(data);
        for(var key in groupKeyValue){
            var optgroup = "<optgroup label=" + groupKeyValue[key] + ">";
            for(var i = 0; i < data.length; i++){
                if(data[i][parentKeyName] == key){
                    optgroup += "<option value=" + data[i][valueKey] + ">" + data[i][labelKey]+ "</option>";
                }
            }
            optgroup += "</optgroup>";
            node.append(optgroup);
        }
    }

    /**
     * 给下拉框节点重置选项
     * @param data 数据源
     * @param node 下拉框节点Jquery对象
     * @param valueKey 选项中value在data数据中的属性名
     * @param labelKey 选项中label在data数据中的属性名
     */
    coreUtil.loadSelectByData = function (data, node, valueKey, labelKey) {
        node.empty();
        node.append("<option value=''>请选择</option>");
        data = JSON.parse(data);
        for(var i = 0; i < data.length; i ++){
            node.append("<option value='" + data[i][valueKey] + "'> " + data[i][labelKey] + "</option>");
        }
    };
	
	/**
     * 创建表格
	 * @param node 表格节点对象
     * @param weekDayNum 周上课天数
     * @param amNum 上午课程数
     * @param pmNum 下午课程数
     * @param nightNum 晚上课程数
     */
	coreUtil.createTable = function (node, weekDayNum, amNum, pmNum, nightNum){

        node.empty();
        var html = "";
        var head = "<thead><tr style='background-color: #007c6e; color: white'><th colspan='2' style='width: 60px;'></th>";
		for(var i = 0; i < weekDayNum; i ++){
            switch (i) {
                case 0:
                    head += "<th width='120px'>星期一</th>";
                    break;
                case 1:
                    head += "<th width='120px'>星期二</th>";
                    break;
                case 2:
                    head += "<th width='120px'>星期三</th>";
                    break;
                case 3:
                    head += "<th width='120px'>星期四</th>";
                    break;
                case 4:
                    head += "<th width='120px'>星期五</th>";
                    break;
                case 5:
                    head += "<th width='120px'>星期六</th>";
                    break;
                case 6:
                    head += "<th width='120px'>星期日</th>";
                    break;
            }

        }
        head += "</tr></thead>";
        html += head;
		html += "<tbody>";
		var am = "";
        var num = 1;
        //添加
        for(var i = 1; i <= amNum; i++){
            if(i == 1){
                am += "<tr><td rowspan='" + amNum + "' width='30px;'>上午</td><td width='30px' height='60px;'>" + num + "</td>";
            }else{
                am += "<tr><td height='60px;'>" + num + "</td>";
            }
            for(var j = 1; j <= weekDayNum; j ++){
                am += "<td></td>";
            }
            am += "</tr>";
            num ++;
        }
//        am += "<tr style='background-color: #D3D4D3'><td colspan='" + (weekDayNum + 2) + "'></td></tr>";

        html += am;

        var pm = "";
        for(var i = 1; i <= pmNum; i++){
            if(i == 1){
                pm += "<tr><td rowspan='" + pmNum + "'>下午</td><td height='60px;'>" + num + "</td>";
            }else{
                pm += "<tr><td height='60px;'>" + num + "</td>";
            }
            for(var j = 1; j <= weekDayNum; j ++){
                pm += "<td></td>";
            }
            pm += "</tr>";
            num ++;
        }
//        pm += "<tr style='background-color: #D3D4D3'><td colspan='" + (weekDayNum + 2) + "'></td></tr>";
        html += pm;


        var night = "";
        for(var i = 1; i <= nightNum; i++){
            if(i == 1){
                night += "<tr><td rowspan='" + nightNum + "'>晚上</td><td height='60px;'>" + num + "</td>";
            }else{
                night += "<tr><td height='60px;'>" + num + "</td>";
            }
            for(var j = 1; j <= weekDayNum; j ++){
                night += "<td></td>";
            }
            night += "</tr>";
            num ++;
        }
        html += night;
		html += "</tbody>";
        node.append(html);
	};
	
	
	coreUtil.setTableTdHtml = function(node, content, col, row){
		node.find('tbody').find('tr:eq(' + (row - 1) + ')').find('td:eq(' + (col ) + ')').html(content);
	};





    /**
     * 加载课表表格  ---表格的格子的ID是preId + row + col---preId最好是定一个长度，方便截取
     * @param weekDayNum 周上课天数
     * @param amNum 上午课程数
     * @param pmNum 下午课程数
     * @param nightNum 晚上课程数
     * @param node 表格节点对象
     * @param idPre 单元格ID的前缀  ---在一个页面上需要用到多个表格的时候，可以添加前缀
     */
    coreUtil.loadCourseTable = function (weekDayNum, amNum, pmNum, nightNum, node, idPre) {
        if(CoreUtil.isEmpty(idPre)){
            idPre = "";
        }
        node.empty();
        var html = "";
        var head = "<thead><tr style='background-color: #007c6e; color: white'><th colspan='2' style='width: 60px;'></th>";
        for(var i = 0; i < weekDayNum; i ++){
            switch (i) {
                case 0:
                    head += "<th width='120px'>星期一</th>";
                    break;
                case 1:
                    head += "<th width='120px'>星期二</th>";
                    break;
                case 2:
                    head += "<th width='120px'>星期三</th>";
                    break;
                case 3:
                    head += "<th width='120px'>星期四</th>";
                    break;
                case 4:
                    head += "<th width='120px'>星期五</th>";
                    break;
                case 5:
                    head += "<th width='120px'>星期六</th>";
                    break;
                case 6:
                    head += "<th width='120px'>星期日</th>";
                    break;
            }

        }
        head += "</tr></thead>";
        html += head;
        var am = "";
        var num = 1;
        //添加
        for(var i = 1; i <= amNum; i++){
            if(i == 1){
                am += "<tr><td rowspan='" + amNum + "' width='30px;'>上午</td><td width='30px' height='60px;'>" + num + "</td>";
            }else{
                am += "<tr><td height='60px;'>" + num + "</td>";
            }
            for(var j = 1; j <= weekDayNum; j ++){
                am += "<td id='" + idPre + num + j + "'></td>";
            }
            am += "</tr>";
            num ++;
        }
        am += "<tr style='background-color: #D3D4D3'><td colspan='" + (weekDayNum + 2) + "'></td></tr>";
        html += am;

        var pm = "";
        for(var i = 1; i <= pmNum; i++){
            if(i == 1){
                pm += "<tr><td rowspan='" + pmNum + "'>下午</td><td height='60px;'>" + num + "</td>";
            }else{
                pm += "<tr><td height='60px;'>" + num + "</td>";
            }
            for(var j = 1; j <= weekDayNum; j ++){
                pm += "<td id='" + idPre + num + j + "'></td>";
            }
            pm += "</tr>";
            num ++;
        }
        pm += "<tr style='background-color: #D3D4D3'><td colspan='" + (weekDayNum + 2) + "'></td></tr>";
        html += pm;


        var night = "";
        for(var i = 1; i <= nightNum; i++){
            if(i == 1){
                night += "<tr><td rowspan='" + nightNum + "'>晚上</td><td height='60px;'>" + num + "</td>";
            }else{
                night += "<tr><td height='60px;'>" + num + "</td>";
            }
            for(var j = 1; j <= weekDayNum; j ++){
                night += "<td id='" + idPre + num + j + "'></td>";
            }
            night += "</tr>";
            num ++;
        }
        html += night;

        node.append(html);
    };
    
    
    coreUtil.clearCourseTable = function (node) {
        node.find();
    }
    






    return coreUtil;
})(CoreUtil, window);
