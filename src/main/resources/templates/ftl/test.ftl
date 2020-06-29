
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="Content-Style-Type" content="text/css"/>
    <title>文档标题</title>
    <style type="text/css">

        body{
            margin: 0 45px;
            font-family: Arial Unicode MS;
            font-size: 12px;
            color: #000;
        }
        .document_body h1 ,
        .document_body h2 ,
        .document_body h3 ,
        .document_body h4 ,
        .document_body h5 ,
        .document_body h6 ,
        .document_body p{
            width:100%;
            height: 100%;
            color: #000;
            margin: 0;
            font-weight: normal;
            text-indent: 0;
        }
        .document_body .logo_position{
            width: 20%;
            position: absolute;
            top: 20px;
            left: 0;
        }
        .document_body .logo_position >img{width: 100%;}
        .document_body{
            padding-top: 40px;
            margin: 0 auto;
            position: relative;

        }
        .document_body p{
            text-align: justify;
            text-justify: inter-word;
        }
        .document_head{margin-bottom: 10px;}
        .document_head .heading{text-align: right;}
        .document_head .heading h1{
            font-size: 20px;
            line-height: 36px;
            text-align: right;
            font-weight: bold;
        }
        .document_head .heading h6{
            text-align: right;
            font-weight: bold;
            font-size: 12px;
        }
        table{
            text-align: left;
            margin-bottom: 10px;
        }
        table .table_title{
            background: #999;
            color: #fff;
            font-size: 14px;
            text-align: center;
            font-weight: bold;
        }
        table .strong_area{
            background: #FFEC00;
        }
        span.line_input{
            display: inline-block;
            border-bottom: 1px solid #000;
            padding: 0 6px;
            margin-left: 2px;
        }
        .document_body .document_main .blank{font-family: "Times New Roman"}
        .sign_area{
            margin-top: 10px;
        }
        .sign_area > div{
            float: left;
        }
        .sign_area > div >b{
            display: block;
            height: 30px;
            font-weight: normal;
        }
        strong{
            font-weight: bold;
            color: red;
        }
        .explain_text p{line-height: 1.5}
        .explain_text h5{font-weight: bold;}
        .clear{clear: both;}
        .mr10{margin-right: 10px;}
        .link a{
            font-weight: bold;
            color: #000;
            font-size: 13px;
        }
        .document_body .side_tips{
            position: absolute;
            top:400px;
            right: -18px;
            width: 10px;
        }
        td {
            text-align: center;
        }
    </style>
</head>
<body>
<!--文档start-->
<div class="document_body">

    <div class="document_main" style="margin-top: 10px">
        <table cellspacing="0" border="1" cellpadding="4" >

            <tr class="link" style="height: 100px">
                <td>test</td>
                <td  colspan="2" >${data.testData}</td>
            </tr>
        </table>

    </div>

</div>
</body>
</html>
