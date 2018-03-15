<html>
<body>
<h2>Hello World!</h2>
spring mvc上传文件
<form name="form1" action="/mall/manager/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" name="上传文件">
</form>

富文本上传文件
<form name="form2" action="/mall/manager/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" name="上传文件">
</form>
</body>
</html>
