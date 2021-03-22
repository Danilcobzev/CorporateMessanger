<#import "parts/common.ftl" as c>
<#import "parts/login_logout.ftl" as l>

<@c.page>
Add new user
${message?ifExists}
<@l.login "/registration" />
</@c.page>