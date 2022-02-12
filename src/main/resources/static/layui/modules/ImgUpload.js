layui.define(['jquery','upload'], function (exports) {
    var ImgUpload = function (elem) {
        var $ = jQuery = layui.jquery;
        var upload = layui.upload;
        /**
         * 多图上传
         */
        upload.render({
            elem: elem
            ,url: '/api/upload/file/qiniu'//接口url
            ,acceptMime: 'image/*'
            ,done: function(res){
                console.log(res);
                if(res.code==0){
                    var item = this.item;

                    var numBox = item.siblings('.pic-num');
                    var num_spans = numBox.children("span");
                    var num_max = num_spans.eq(1).html();
                    var num_min = num_spans.eq(0).html();

                    console.log(item);
                    item.before("<div class=\"pic_box\">\n" +
                        "                            <i class=\"layui-icon layui-icon-close-fill  removeImg\" data-index=\""+num_min+"\"></i>\n" +
                        "                            <img class=\"background\" src='"+res.data.src+"'>\n" +
                        "                        </div>");
                    var input = item.siblings('.value-input');
                    console.log(input);
                    var val = $(input).val();
                    console.log(val);
                    if(val==''){
                        $(input).val(res.data.src);
                    }else{
                        $(input).val(val+','+res.data.src);
                    }
                    if(++num_min>=num_max){
                        item.hide()
                    }
                    num_spans.eq(0).html(num_min);
                }
            }
        });
        /**
         * 多图删除
         */

        $('body').delegate(".removeImg","click",function(){
            var index = $(this).data("index")
            var picBox = $(this).parent('.pic_box');
            console.log(picBox);
            var input = $(picBox).siblings('.value-input');
            var val = $(input).val();
            var val_url = val.split(",");
            val_url.splice(index, 1);

            var addImg = $(picBox).siblings('.addImg');
            var numBox = $(picBox).siblings('.pic-num');
            var num_spans = numBox.children("span");
            var num_max = num_spans.eq(1).html();
            var num_min = num_spans.eq(0).html();
            num_spans.eq(0).html(--num_min)
            $(addImg).show();
            $(picBox).remove();
            $(input).val(val_url.join(","));
        })
    };
    exports('ImgUpload', ImgUpload);
})