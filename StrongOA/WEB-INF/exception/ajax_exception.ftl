${response.setContentType("text/plain;charset=UTF-8")}
<#if Request["extMessage"]?exists>
${Request["extMessage"]!""}
</#if>