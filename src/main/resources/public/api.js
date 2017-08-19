Vue.mixin({
    methods: {
        getUserListPage: function (params) {
            return axios.get("/api/user/list", {params: params});
        }
    }
})


var treeter = {
    methods: {
        findFromTree: function (treeArray, id, idPropName, childrenPropName) {
            idPropName = (idPropName ? idPropName : "id");
            childrenPropName = (childrenPropName ? childrenPropName : "children");
            if (!treeArray || treeArray == null || treeArray.length <= 0)return null;
            for (var i = 0; i < treeArray.length; i++) {
                if (treeArray[i][idPropName] == id) {
                    return treeArray[i];
                } else {
                    const result = findFromTree(treeArray[i][childrenPropName], id, idPropName, childrenPropName);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        },
        appendTreeNode: function (treeArray, item, idPropName, parentPropName, childrenPropName) {
            idPropName = (idPropName ? idPropName : "id");
            parentPropName = (parentPropName ? parentPropName : "parentId");
            childrenPropName = (childrenPropName ? childrenPropName : "children");

            if (treeArray == null || treeArray.length <= 0)return;
            if (!item[parentPropName] || item[parentPropName] == null) {
                var i = treeArray.findIndex(function (p) {
                    p.sort > item.sort
                });
                if (i == -1) {
                    i = treeArray.length;
                }
                treeArray.splice(i, 0, item);
                return;
            }
            for (var j = 0; j < treeArray.length; j++) {
                var value = treeArray[j];
                if (item[parentPropName] == value[idPropName]) {
                    if (value[childrenPropName] && value[childrenPropName].length > 0) {
                        var i = value[childrenPropName].findIndex(function (p) {
                            p.sort > item.sort
                        });
                        if (i == -1) {
                            i = value[childrenPropName].length;
                        }
                        value[childrenPropName].splice(i, 0, item);
                    } else {
                        value[childrenPropName] = [];
                        value[childrenPropName].push(item);
                    }
                } else {
                    appendTreeNode(value[childrenPropName], item, idPropName, parentPropName, childrenPropName);
                }
            }
        },
        deleteFromTree: function (list, id, idPropName, childrenPropName) {
            idPropName = (idPropName ? idPropName : "id");
            childrenPropName = (childrenPropName ? childrenPropName : "children");
            if (!list || list == null || list.length <= 0)return true;
            for (var i = 0; i < list.length; i++) {
                if (list[i][idPropName] == id) {
                    list.splice(i, 1);
                    return true;
                } else {
                    var result = deleteFromTree(list[i][childrenPropName], id, idPropName, childrenPropName);
                    if (result) {
                        return result;
                    }
                }
            }
            return false;
        },
        updateTreeNode: function (list, item, idPropName, childrenPropName) {
            idPropName = (idPropName ? idPropName : "id");
            childrenPropName = (childrenPropName ? childrenPropName : "children");
            if (!list || list == null || list.length <= 0) return false;
            for (var i = 0; i < list.length; i++) {
                if (list[i][idPropName] == item[idPropName]) {
                    console.log(list[i][idPropName], item[idPropName]);
                    list.splice(i, 1, item);
                    return true;
                } else {
                    var result = updateTreeNode(list[i][childrenPropName], item, idPropName, childrenPropName);
                    if (result) {
                        return result;
                    }
                }
            }
            return false;
        },

        batchDeleteFromTree: function (list, ids, idPropName, childrenPropName) {
            idPropName = (idPropName ? idPropName : "id");
            childrenPropName = (childrenPropName ? childrenPropName : "children");
            if (!list || list == null || list.length <= 0)return;
            if (!ids || ids == null || ids.length <= 0)return;
            for (var i = 0; i < list.length; i++) {
                if (ids.findIndex(function (x) {
                        x == list[i][idPropName] > -1
                    })) {
                    list.splice(i, 1);
                    i--;
                    continue;
                } else {
                    batchDeleteFromTree(list[i][childrenPropName], ids, idPropName, childrenPropName);
                }
            }
        }
    }
};
const CONTEXT = '';
const mergeData =function(target) {
    for (var i = 1, j = arguments.length; i < j; i++) {
        var source = arguments[i] || {};
        for (var prop in source) {
            if (source.hasOwnProperty(prop)) {
                var value = source[prop];
                if (value !== undefined) {
                    target[prop] = value;
                }
            }
        }
    }

    return target;
};
var api= {
  
// const CONTEXT = './Vue-Admin';

  LOGIN :CONTEXT + '/login',
  LOGOUT :CONTEXT + '/logout',
  CHANGE_PWD :CONTEXT + '/changePwd',

  SYS_MENU_GET :CONTEXT + '/sys/menu/get',
  SYS_MENU_UPDATE :CONTEXT + '/sys/menu/update',
  SYS_MENU_DELETE :CONTEXT + '/sys/menu/delete',
  SYS_MENU_ADD :CONTEXT + '/sys/menu/add',
  SYS_MENU_PAGE :CONTEXT + '/sys/menu/page',
  SYS_MENU_LIST :CONTEXT + '/sys/menu/list',
  SYS_MENU_LIST2 :CONTEXT + '/sys/menu/list2',

  SYS_ROLE_GET :CONTEXT + '/sys/role/get',
  SYS_ROLE_UPDATE :CONTEXT + '/sys/role/update',
  SYS_ROLE_DELETE :CONTEXT + '/sys/role/delete',
  SYS_ROLE_ADD :CONTEXT + '/sys/role/add',
  SYS_ROLE_PAGE :CONTEXT + '/sys/role/page',
  SYS_ROLE_LIST :CONTEXT + '/sys/role/list',
  SYS_ROLE_LIST2 :CONTEXT + '/sys/role/list2',
  SYS_ROLE_RESOURCE :CONTEXT + '/sys/role/resources',
  SYS_SET_ROLE_RESOURCE :CONTEXT + '/sys/role/setResources',

  SYS_RESOURCE_GET :CONTEXT + '/sys/resource/get',
  SYS_RESOURCE_UPDATE :CONTEXT + '/sys/resource/update',
  SYS_RESOURCE_DELETE :CONTEXT + '/sys/resource/delete',
  SYS_RESOURCE_ADD :CONTEXT + '/sys/resource/add',
  SYS_RESOURCE_PAGE :CONTEXT + '/sys/resource/page',
  SYS_RESOURCE_LIST :CONTEXT + '/sys/resource/list',
  SYS_RESOURCE_LIST2 :CONTEXT + '/sys/resource/list2',

  SYS_USER_GET :CONTEXT + '/sys/user/get',
  SYS_USER_ADD :CONTEXT + '/sys/user/add',
  SYS_USER_UPDATE :CONTEXT + '/sys/user/update',
  SYS_USER_DELETE :CONTEXT + '/sys/user/delete',
  SYS_USER_PAGE :CONTEXT + '/sys/user/page',
  SYS_USER_ROLE :CONTEXT + '/sys/user/roleIds',
  SYS_SET_USER_ROLE :CONTEXT + '/sys/user/setRoles',


  UPLOAD_FORM_FILE :CONTEXT + '/upload/imageUpload',
  UPLOAD_FORM_FILE_TEMP :CONTEXT + '/upload/formFile',
  UPLOAD_FORM_FILE_BATCH :CONTEXT + '/upload/batch',

  MSG_TOP_TEN :CONTEXT + '/msg/topTen',
  TEST_DATA :CONTEXT + '/static/data/data.json'
}