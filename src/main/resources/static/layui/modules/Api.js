layui.define(['jquery'],function(exports){
    let pathParams = function (url, params = {}) {
        return Object.keys(params).map(function(key) {
            return url.replace(/(\\)?\{([^\{\}\\]+)(\\)?\}/g, params[key])
        }).join();
    };

    let ApiConfig ={
        uploadFileApi:"/api/upload/file/qiniu",
        //admin
        adminApi:"/manager/admin",
        adminRoleApi:"/manager/admin/role",
        adminPwdApi:"/manager/admin/psd",
        //menu
        menuApi:"/manager/menu",
        menuDetailApi:"/manager/menu/{id}",
        menuHomeApi:"/manager/menu/home",
    };

    var request =  function(url,params,type){
        return new Promise((resolve, reject) => {
            var roleSaveLoading = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false, shade: 0.8});
            var $ = layui.jquery;
            $.ajax({
                url: url,
                cache: false,
                data: params||{},
                type: type == undefined ? "GET" : type,
                contentType: 'application/json; charset=UTF-8',
                dataType: "json",
                success: function (res) {
                    top.layer.close(roleSaveLoading);
                    switch (res.code) {
                        case 0:
                            resolve(res);
                            break;
                        case 1:
                            top.layer.msg(res.msg, {
                                offset: 't',
                                anim: 6,
                            }, function () {
                                top.location = ""
                            });
                            reject(res);
                            break;
                        case 400:
                            top.layer.msg(res.msg, {
                                offset: 't',
                                anim: 6,
                            });
                            reject(res);
                            break;
                        case 403:
                            window.location.href = "/index/403"
                            reject(res);
                            break;
                        default:
                            // top.layer.msg(res.msg, {
                            //     offset: 't',
                            //     anim: 6,
                            // }, function () {
                            //     window.location.href = "/index/500"
                            // });
                            reject(res);
                            break;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    top.layer.close(roleSaveLoading);
                    // if (XMLHttpRequest.status == 404) {
                    //     location.href = "/index/404";
                    // } else {
                    //     layer.msg("服务器好像除了点问题！请稍后试试", function () {
                    //         window.location.href = "/index/500"
                    //     });
                    // }
                    reject(XMLHttpRequest);
                },
                complete:function () {
                    top.layer.close(roleSaveLoading);
                }
            });
        })
    };

    var api = {
        //admin
        getAdmins:data=>request(ApiConfig.adminApi,data,"GET"),
        addAdmin:data=>request(ApiConfig.adminApi,data,"POST"),
        updateAdmin:data=>request(ApiConfig.adminApi,data,"PUT"),
        deleteAdmin:data=>request(ApiConfig.adminApi,data,"DELETE"),
        getAdminRole:data=>request(ApiConfig.adminRoleApi,data,"GET"),
        setAdminRole:data=>request(ApiConfig.adminRoleApi,data,"PUT"),
        updatePwd:data=>request(ApiConfig.adminPwdApi,data,"PUT"),
        //role
        //menu
        getMenu:data=>request(pathParams(ApiConfig.menuDetailApi,data),data,"get"),
        updateMenu:data=>request(ApiConfig.menuApi,data,"put"),
        addMenu:data=>request(ApiConfig.menuApi,data,"post"),
        deleteMenu:data=>request(ApiConfig.menuApi,data,"DELETE"),

    };
    exports('Api', Object.assign(api, ApiConfig));
});



