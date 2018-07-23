<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>TreeSoft数据库管理系统</title>
<meta name="Keywords" content="Treesoft数据库管理系统">
<meta name="Description" content="Treesoft数据库管理系统">
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<%@ include file="/WEB-INF/views/include/codemirror.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<!--导入首页启动时需要的相应资源文件(首页相应功能的 js 库、css样式以及渲染首页界面的 js 文件)-->
<script src="${ctx}/static/plugins/easyui/common/index.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/easyui/common/indexSearch.js" type="text/javascript"></script>
<link href="${ctx}/static/plugins/easyui/common/index.css" rel="stylesheet" />
<script src="${ctx}/static/plugins/easyui/common/index-startup.js"></script>

<link type="text/css" rel="stylesheet" href="${ctx}/static/css/eclipse.css">  
<link type="text/css" rel="stylesheet" href="${ctx}/static/css/codemirror.css" />  
<link type="text/css" rel="stylesheet" href="${ctx}/static/css/show-hint.css" /> 
<link rel="icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">  
<link rel="shortcut icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">
<script type="text/javascript" src="${ctx}/static/js/codemirror.js"></script>  

<script type="text/javascript" src="${ctx}/static/js/sql.js"></script>  
<script type="text/javascript" src="${ctx}/static/js/show-hint.js"></script>  
<script type="text/javascript" src="${ctx}/static/js/sql-hint.js"></script>  
<style>  
        .CodeMirror {  
            border: 1px solid #cccccc; 
            height: 98%;
        }  
</style> 

</head>
<body>
	<!-- 容器遮罩 -->
    <div id="maskContainer">
        <div class="datagrid-mask" style="display: block;"></div>
        <div class="datagrid-mask-msg" style="display: block; left: 50%; margin-left: -52.5px;">
            正在加载...
        </div>
    </div>
    
    <div id="mainLayout" class="easyui-layout hidden" data-options="fit: true">
    
        <div id="northPanel" data-options="region: 'north', border: false" style="height: 80px; overflow: hidden;">
           
            <div id="topbar" class="top-bar"  style="width: 100%;height:52px; background: #0092dc url('${ctx}/static/images/mosaic-pattern.png') repeat;">

                 <div class="top-bar-left">
                    <h1 style="margin-left: 10px; margin-top: 10px;color: #fff"> <img src="${ctx}/static/images/logo.png" >TreeSoft数据库管理系统<span style="color:#00824D;font-size:14px; font-weight:bold;">&nbsp;TreeNMS</span>  <span style="color: #fff;font-size:12px;">V1.7.2</span> </h1>
                </div>
                
                <div class="top-bar-right"  >
                    <div id="timerSpan"> 
                    
                     <div id="operator" style="padding:5px;height:auto">
                       <div style="padding-right:10px;height:auto">
                         
                         <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/alarm.gif" onclick="javascript:infoData()"  title="实时状态监控"/>
                         </div> 
                         
                          <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/btn_hd_backup.gif" onclick="javascript:backupDatabase()"  title="备份/还原"/>
                         </div> 
                         
                         <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/btn_json.gif" onclick="javascript:jsonFormat()"  title="Json格式化"/>
                          </div>
                          
                          <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/btn_hd_heart.gif" onclick="javascript:contribute()"  title="捐赠"/>
                          </div> 
                           
                           <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/btn_hd_support.gif" onclick="javascript:ShowConfigPage()"  title="数据库配置"/>
                          </div>  
                         
                           <div style="padding-right:20px; display:inline;cursor:pointer;" >
                               <img    src="${ctx}/static/images/btn_hd_pass.gif"  onclick="javascript:ShowPasswordDialog()"  title="修改用户密码"/>
                          </div>  
                           
                          <div style="padding-right:20px; display:inline;cursor:pointer;">
                             <img   src="${ctx}/static/images/btn_hd_help.gif" onclick="javascript:help()"  title="帮助"/>
                          </div>
                          <div style=" display:inline;cursor:pointer; ">
                             <img id="btnExit"  src="${ctx}/static/images/btn_hd_exit.gif" title="注销"/> 
	       		          </div> 
	       		          
	       		      </div> 
                      </div>
                    </div>
                    
                    <div id="themeSpan">
                        <a id="btnHideNorth" class="easyui-linkbutton" data-options="plain: true, iconCls: 'layout-button-up'"> </a>
                    </div>
                </div>
            </div>
            
            <div id="toolbar" class="panel-header panel-header-noborder top-toolbar">
                <div id="infobar">
                    <span class="icon-hamburg-user" style="padding-left: 25px; background-position: left center;">
                      ${username}，您好
                    </span>
                </div>
               
                <div id="buttonbar">
                    <a href="javascript:void(0);"  id="btnFullScreen" class="easyui-linkbutton easyui-tooltip" title="全屏切换" data-options="plain: true, iconCls: 'icon-standard-arrow-inout'"  >全屏切换</a> 
                
                    <span>更换皮肤：</span>
                    <select id="themeSelector"></select>					
                    <a id="btnShowNorth" class="easyui-linkbutton" data-options="plain: true, iconCls: 'layout-button-down'" style="display: none;"></a>
               
                </div>
            </div>
        </div>

        <div data-options="region: 'west', title: '数据库选择', iconCls: 'icon-standard-map', split: true, minWidth: 200, maxWidth: 400" style="width: 220px; padding: 1px;">
			  
			<div id="eastLayout" class="easyui-layout" data-options="fit: true">
                <div data-options="region: 'north', split: false, border: false" style="height: 34px;">
                    <select class="combobox-f combo-f" style="width:180px;margin:5px; " id="databaseSelect"  >   </select> 
                </div>
                
                <div   data-options="region: 'center', border: false, title: '数据库', iconCls: 'icon-hamburg-database', tools: [{ iconCls: 'icon-hamburg-refresh', handler: function () {  dg.treegrid('reload'); } }]">
                       <input id="pid" name="pid" />  
                </div>
            </div>
			  
        </div>

        <div data-options="region: 'center'">
            <div id="mainTabs_tools" class="tabs-tool">
                <table>
                    <tr>
                        <td><a id="mainTabs_jumpHome" class="easyui-linkbutton easyui-tooltip" title="跳转至主页选项卡" data-options="plain: true, iconCls: 'icon-hamburg-home'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
						<td><a id="mainTabs_toggleAll" class="easyui-linkbutton easyui-tooltip" title="展开/折叠面板使选项卡最大化" data-options="plain: true, iconCls: 'icon-standard-arrow-out'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
                        <td><a id="mainTabs_refTab" class="easyui-linkbutton easyui-tooltip" title="刷新当前选中的选项卡" data-options="plain: true, iconCls: 'icon-standard-arrow-refresh'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
                        <td><a id="mainTabs_closeTab" class="easyui-linkbutton easyui-tooltip" title="关闭当前选中的选项卡" data-options="plain: true, iconCls: 'icon-standard-application-form-delete'"></a></td>
                    </tr>
                </table>
            </div>

            <div id="mainTabs" class="easyui-tabs" data-options="fit: true, border: false, showOption: true, enableNewTabMenu: true, tools: '#mainTabs_tools', enableJumpTabMenu: true">
                <div id="homePanel" data-options="title: '主页-系统状态', iconCls: 'icon-hamburg-home'">
                     <div id="tb" style="padding:5px;height:auto">
                         <div>
	       		            
	                       <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-bricks" plain="true" onclick="infoData()">实时状态监控</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                           <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-arrow-refresh" plain="true" onclick="refresh2()">刷新</a>
	                       <span class="toolbar-item dialog-tool-separator"></span>
                         </div> 
                     </div>
  
                    <table id="dg2"></table> 
                </div>
            </div>
        </div>
        
        <div data-options="region: 'east', title: '常用SQL', iconCls: 'icon-standard-book', split: true,collapsed: true, minWidth: 160, maxWidth: 500" style="width: 220px;">
            <div id="eastLayout" class="easyui-layout" data-options="fit: true">
               
                <div data-options="region: 'north', split: true, border: false" style="height:800px">
                     <input id="sqlStudyList"   />  
                </div>
                
                <%--<div id="searchHistoryPanel" data-options="region: 'center', split: true,  border: false, title: '我的键值设置', iconCls: 'icon-standard-book-key', tools: [{ iconCls: 'icon-hamburg-refresh', handler: function () {  searchBG.treegrid('reload'); } }]">
                       <input id="searchHistoryList"   />  
                </div>  --%>
          
            </div>
        </div>

        <div data-options="region: 'south', title: '关于...', iconCls: 'icon-standard-information', collapsed: true, border: false" style="height: 70px;">
            <div style="color: #4e5766; padding: 6px 0px 0px 0px; margin: 0px auto; text-align: center; font-size:12px; font-family:微软雅黑;">
              <img src="http://www.treesoft.cn/picture/logo.png" onerror="imgerror(this)"> CopyRight@2016 版权所有 <a href="http://www.treesoft.cn" target="_blank" style="text-decoration:none;" > www.treesoft.cn  </a>  &nbsp; Email: treesoft@qq.com
              &nbsp; 在未购买授权的情况下，我们不承担因软件问题造成的任何经济损失。 
            </div>
        </div>
    </div>  
  
  <div id='tb3' style='padding:5px;height:auto'>    
    <div  >    
        <a href='#' class='easyui-linkbutton' iconCls='icon-add' plain='true'></a>    
        <a href='#' class='easyui-linkbutton' iconCls='icon-edit' plain='true'></a>           
    </div>  
</div> 
  
<div id="dlgg" ></div>  
<div id="addRow" ></div> 
<input type="hidden" id="currentTableName" >

</body>
<script type="text/javascript" src="${ctx}/static/js/index.treenms.js"></script> 
</html>
