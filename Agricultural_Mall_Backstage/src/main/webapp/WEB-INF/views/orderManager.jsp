<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
    <title>backend</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/js/userSetting.js"></script>
    <script src="${pageContext.request.contextPath}/layer/layer.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrapValidator.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap-paginator.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mall.css"/>

    <script>

        $(function () {

            /*分页插件的初始化*/
            $('#pagination').bootstrapPaginator({
                bootstrapMajorVersion: 3,
                currentPage: ${pageInfo.pageNum},
                totalPages: ${pageInfo.pages},
                numberOfPages:${pageInfo.pageSize},
                itemTexts: function (type, page, current) {
                    switch (type) {
                        case 'first':
                            return '首页';
                        case 'prev':
                            return '上一页';
                        case 'next':
                            return '下一页';
                        case 'last':
                            return '末页';
                        case 'page':
                            return page;
                    }
                },
                <%--pageUrl: function (type, page, current) {--%>
                <%--return '${pageContext.request.contextPath}/admin/system_user/manager/getAllSystemUsers?pageNum='+ page;--%>
                <%--}--%>
                pageUrl: function (type, page, current) {
                    return '${pageContext.request.contextPath}/admin/order/getAllOrders?pageNum=' + page;
                }

            });

            //显示服务端传过来的消息
            var successMsg = '${successMsg}';
            var failMsg = '${failMsg}';

            if (successMsg != '') {
                layer.msg(successMsg, {
                    time: 1000,
                    //设置他弹出层的宽度，高度自适应
                    area: '100px',
                    skin: 'successMsg'
                });
            }

            if (failMsg != '') {
                layer.msg(failMsg, {
                    time: 1500,
                    //设置他弹出层的宽度，高度自适应
                    area: '100px',
                    skin: 'errorMsg'
                });
            }

        });

        //修改订单信息时展示订单信息
        function showOrder(id) {
            $.post(
                '${pageContext.request.contextPath}/admin/order/findOrderById',
                {'id': id},
                function (result) {
                    if (result.status == 1) {
                        $('#id').val(result.data.id);
                        $('#orderNumber').val(result.data.orderNumber);
                        $('#customerID').val(result.data.customer.id);
                        $('#createDate').val(result.data.createDate);
                        $('#status').val(result.data.status);
                    }
                }
            );
        }

        //显示删除订单模态框
        function showDeleteModal(id) {
            //将id存放到隐藏域中，交给弹出框这个页面中
            $('#deleteOrderId').val(id);
            $('#deleteOrderModal').modal('show');
        }

        //删除订单信息
        function deleteOrder(id) {
            $.post(
                '${pageContext.request.contextPath}/admin/order/removeOrderById',
                {'id': id},
                function (result) {
                    if (result.status == 1) {
                        layer.msg(result.message, {
                            time: 700,
                            skin: "successMsg",
                        }, function () {
                            //删除成功，重新加载页面数据
                            if (${pageInfo.size > 1}) {
                                location.href = '${pageContext.request.contextPath}/admin/order/getAllOrders?pageNum=' +${pageInfo.pageNum};
                            } else {
                                location.href = '${pageContext.request.contextPath}/admin/order/getAllOrders';
                            }
                        })
                    } else {
                        layer.msg(result.message, {
                            time: 700,
                            skin: "errorMsg"
                        });
                    }
                }
            );
        }

    </script>

</head>

<body>

<div class="panel panel-default" id="userInfo" id="homeSet">
    <div class="panel-heading">
        <h3 class="panel-title">订单管理</h3>
    </div>
    <div class="panel-body">
        <div class="showusersearch">
            <form class="form-inline" action="${pageContext.request.contextPath}/admin/order/getAllOrdersByParams"
                  method="post" id="frmSearch">
                <div class="form-group">
                    <input type="hidden" ordernumber="pageNum" value="${pageInfo.pageNum}" id="pageNum">
                    <label for="order_orderNumber">订单号:</label>
                    <input type="text" class="form-control" id="order_orderNumber" name="orderNumber"
                           placeholder="请输入订单号"
                           value="${params.orderNumber}" size="15px">
                </div>
                <div class="form-group">
                    <label for="order_customerId">客户帐号:</label>
                    <input type="text" class="form-control" id="order_customerId" name="customerId" placeholder="请输入帐号"
                           value="${params.customerId}" size="15px">
                </div>
                <div class="form-group">
                    <label for="order_status">状态:</label>
                    <select class="form-control" id="order_status" name="status">
                        <option value="-1">全部</option>
                        <option value="0" <c:if test="${params.status == 0}">selected</c:if>>---未支付---</option>
                        <option value="1" <c:if test="${params.status == 1}">selected</c:if>>---已支付---</option>
                        <option value="2" <c:if test="${params.status == 2}">selected</c:if>>---未发货---</option>
                        <option value="3" <c:if test="${params.status == 3}">selected</c:if>>---未收货---</option>
                        <option value="4" <c:if test="${params.status == 4}">selected</c:if>>---交易完成---</option>
                        <option value="5" <c:if test="${params.status == 5}">selected</c:if>>---无效---</option>
                    </select>
                </div>
                <input type="submit" value="查询" class="btn btn-primary" id="doSearch">
            </form>
        </div>
        <div class="show-list text-center" style="position: relative;top: 30px;">
            <table class="table table-bordered table-hover" style='text-align: center;'>
                <thead>
                <tr class="text-danger">
                    <th class="text-center">序号</th>
                    <th class="text-center">订单号</th>
                    <th class="text-center">客户帐号</th>
                    <th class="text-center">商品数量</th>
                    <th class="text-center">创建时间</th>
                    <th class="text-center">状态</th>
                    <th class="text-center">操作</th>
                </tr>
                </thead>
                <tbody id="tb">
                <c:forEach items="${pageInfo.list}" var="order">
                    <tr>
                        <td>${order.id}</td>
                        <td>${order.orderNumber}</td>
                        <td>${order.customer.id}</td>
                        <td>${order.productNumber}</td>
                        <td>
                            <fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <c:if test="${order.status == 0}">未支付</c:if>
                            <c:if test="${order.status == 1}">未发货</c:if>
                            <c:if test="${order.status == 2}">已发货</c:if>
                            <c:if test="${order.status == 3}">未收货</c:if>
                            <c:if test="${order.status == 4}">交易完成</c:if>
                            <c:if test="${order.status == 5}">无效</c:if>
                        </td>
                        <td class="text-center">
                            <input type="button" class="btn btn-warning btn-sm doModify"
                                   onclick="showOrder(${order.id})" value="修改">
                            <input type="button" class="btn btn-danger btn-sm doDelete"
                                   onclick="showDeleteModal(${order.id})" value="删除">
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <ul id="pagination"></ul>
        </div>
    </div>
</div>

<!-- 修改订单信息-->
<div class="modal fade" tabindex="-1" id="myModal">
    <!-- 窗口声明 -->
    <div class="modal-dialog">
        <!-- 内容声明 -->
        <form action="${pageContext.request.contextPath}/admin/order/modifyOrder" method="post"
              id="frmModifyOrder">
            <div class="modal-content">
                <!-- 头部 主体 -->
                <div class="modal-header">
                    <button class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">修改订单</h4>
                </div>
                <div class="modal-body text-center">
                    <div class="row text-right">
                        <%--当前页面放到隐藏域中--%>
                        <input type="hidden" name="pageNum" value="${pageInfo.pageNum}">
                        <label for="id" class="col-sm-4 control-label">序&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" readonly id="id" name="id">
                        </div>
                    </div>
                    <br>
                    <div class="row text-right form-group">
                        <label for="orderNumber" class="col-sm-4 control-label">订&nbsp;&nbsp;单&nbsp;&nbsp;号：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" readonly id="orderNumber" name="orderNumber">
                        </div>
                    </div>
                    <br>
                    <div class="row text-right">
                        <label for="customerID" class="col-sm-4 control-label">客户帐号：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" readonly id="customerID" name="customerID">
                        </div>
                    </div>
                    <br>
                    <div class="row text-right">
                        <div class="form-group">
                            <label for="status" class="col-sm-4 control-label">状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态：</label>
                            <div class="col-sm-4">
                                <select class="form-control" name="status" id="status">
                                    <option value="">--请选择--</option>
                                    <option value="0">未支付</option>
                                    <option value="1">未发货</option>
                                    <option value="2">已发货</option>
                                    <option value="3">未收货</option>
                                    <option value="4">交易完成</option>
                                    <option value="5">无效</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <br>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary updateOne">确认</button>
                    <button class="btn btn-primary cancel" data-dismiss="modal">取消</button>
                </div>
            </div>
        </form>
    </div>
</div>
<!-- 修改订单信息 end-->

<!-- 确认删除订单 start-->
<div class="modal fade" tabindex="-1" id="deleteOrderModal">
    <!-- 窗口声明 -->
    <div class="modal-dialog modal-sm">
        <!-- 内容声明 -->
        <div class="modal-content">
            <!-- 头部、主体、脚注 -->
            <div class="modal-header">
                <button class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">提示消息</h4>
            </div>
            <div class="modal-body text-center row">
                <h5 class="text-center text-warning">确认要删除该订单吗？</h5>
            </div>
            <div class="modal-footer">
                <input type="hidden" id="deleteOrderId">
                <button class="btn btn-primary" data-dismiss="modal"
                        onclick="deleteOrder($('#deleteOrderId').val())">确认
                </button>
                <button class="btn btn-primary cancel" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
<!-- 确认删除订单 end-->

</body>

</html>
