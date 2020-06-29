
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
        <!-- 自动换页 -->
        tr {
            page-break-inside:avoid;
            page-break-after:auto;
            page-break-before: auto;
        }
        table{
            text-align: left;
            margin-bottom: 10px;
            page-break-inside:avoid;
            -fs-table-paginate:paginate;
            border-spacing:0;
            table-layout:fixed;
            word- break:break- strict;
            cellspacing:0;
            cellpadding:0 ;
            border: solid 1px #ccc;
            padding: 2px 2px;

        }
    </style>
</head>
<body>
<!--文档start-->
<div class="document_body">

    <div class="document_main" style="margin-top: 10px">
        <table cellspacing="0" border="1" cellpadding="4" >

            <tr class="link" style="height: 100px">
                <td>参保单位名称</td>
                <td  colspan="2" >${data.unitName}</td>
                <td >统一信用代码</td>
                <td>${data.creditCode}</td>
                <td >单位编号</td>
                <td >${data.unitCode}</td>
            </tr>
            <tr   style="height: 100px">
                <td >单位参保缴费情况</td>
                <td>险种类型</td>
                <td>缴费状态</td>
                <td colspan="2">现参保地经办机构</td>
                <td>当前参保人数</td>
                <td>缴费起止时间</td>
            </tr>
            <#list data.unitPayPlanList as unitPayPlan>
                <tr  style="height: 100px">
                    <td >
                        <#if (unitPayPlan.insuredType)??>
                            ${unitPayPlan.insuredType}
                        </#if>
                    </td>
                    <td>
                        <#if (unitPayPlan.payState)??>
                            ${unitPayPlan.payState}
                        </#if>
                    </td>
                    <td colspan="2" >
                        <#if (unitPayPlan.insuredHandleOrg)??>
                            ${unitPayPlan.insuredHandleOrg}
                        </#if>
                    </td>
                    <td>
                        <#if (unitPayPlan.insuredNum)??>
                            ${unitPayPlan.insuredNum}
                        </#if>
                    </td>
                    <td>
                        <#if (data.startTime)?? && (data.endTime)??>
                            ${data.startTime}至${data.endTime}
                        </#if>
                     </td>
                </tr>
            </#list>
        </table>

    </div>
    <div style="text-align:right">
        <div tyle="float:right">打印日期：<span>${data.currentDate}</span></div>
    </div>
    <div>
        <p>提示：</p>

          <p>  1、如对您的参保信息有疑问，请您持本人有效身份证件和本《缴费证明》到现参保地社保经办机构进行核实。</p>

          <p>  2、此证明与现参保地社保经办机构打印的《贵州省社会保险参保缴费证明》具有同等效力。</p>

          <p>  3、如需对此证明进行真伪辨别，请使用微信扫描上方二维码进行识别确认。</p>
    </div>
</div>
</body>
</html>
