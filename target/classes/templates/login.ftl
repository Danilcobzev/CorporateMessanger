<#import "parts/common.ftl" as c>
<#import "parts/login_logout.ftl" as l>

<@c.page>
${message?ifExists}
<@l.login "/login" false/>
</@c.page>