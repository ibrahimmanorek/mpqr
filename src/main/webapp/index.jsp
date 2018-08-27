<%--
  Created by IntelliJ IDEA.
  User: Halim A
  Date: 08/05/2018
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>MPQR Generator</title>

    <link href="<c:url value="/"/>assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value="/"/>assets/css/style.css" rel="stylesheet">
    <link href="<c:url value="/"/>assets/css/zoom.css" rel="stylesheet">

</head>

<body>
<nav class="navbar navbar-expand-lg navbar-dark fixed-top darkmagenta">
    <div class="container">
        <div class="row">
            <div class="col-sm-4"><img src="assets/img/paypro.jpg" height="50" width="50"></div>
            <div class="col-sm-8"><a class="navbar-brand" href="#">MPQR Generator</a></div>
        </div>
    </div>
</nav>

<div class="container">
    <h5 class="my-4 text-center text-lg-left">Upload Your xlsx File Or Download Sample</h5>
    <form:form method="post" class="l-padding-20" enctype="multipart/form-data"
               action="/qrgenerator/uploadFile">

        <div class="form-group">
            <input class="" type="file" name="file" accept=".xlsx"/>
            <input class="btn btn-success" type="submit" value="Upload file"/>
        </div>

        <div class="form-container">
            <a href="<c:url value='/download' />">Download Sample</a>
            <c:if test="${qrModels != null}">
                <a href="<c:url value='/downloadQr' />" class="float-right">Download QR Image</a>
            </c:if>
        </div>

    </form:form>
    <br/>
    <br/>

    <c:if test="${qrModels != null}">
        <nav aria-label="QrImage Pagination">
            <ul class="pagination justify-content-center">

                <c:forEach begin="1" end="${maxPages}" step="1" varStatus="i">
                    <c:choose>
                        <c:when test="${page == i.index}">
                            <span>${i.index}</span>
                        </c:when>
                        <c:otherwise>
                            <c:url value="/qrImages" var="url">
                                <c:param name="page" value="${i.index}"/>
                            </c:url>
                            <li class="page-item">
                                <a href='<c:out value="${url}" />' class="page-link">${i.index}</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

            </ul>
        </nav>
    </c:if>

    <div class="row text-center text-lg-left">
        <c:forEach items="${qrModels}" var="qr">
            <div class="col-lg-3 col-md-4 col-xs-6 ">
                <h5 align="center">${qr.imageName}</h5>
                <a href="#" class="d-block mb-4 h-100">
                    <img class="img-fluid img-thumbnail"
                         src="<c:url value='${qr.imagePath}'/>"
                         height="200" width="300" alt="" data-action="zoom">
                </a>
            </div>
        </c:forEach>
    </div>

</div>
<!-- /.container -->
<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
<!-- Footer -->
<footer class="py-5 darkmagenta">
    <div class="container">
        <p class="m-0 text-center text-white">Copyright &copy; PayPro Indonesia 2018</p>
    </div>
    <!-- /.container -->
</footer>

<!-- Bootstrap core JavaScript -->
<script src="<c:url value="/"/>assets/js/jquery.min.js"></script>
<script src="<c:url value="/"/>assets/js/bootstrap.bundle.min.js"></script>
<script src="<c:url value="/"/>assets/js/zoom.js"></script>
<script src="<c:url value="/"/>assets/js/transition.js"></script>

</body>

</html>

