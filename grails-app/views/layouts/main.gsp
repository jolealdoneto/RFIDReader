<!DOCTYPE html>
<html lang="en" ng-app="ativ3">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
		<g:layoutHead/>
		<r:layoutResources />
	</head>
	<body>
        <div class="container">
            <h1 style="text-align: center;">Atividade 3 - RFID</h1>

            <div ng-view></div>
        </div>

		<g:layoutBody/>
		<r:layoutResources />
	</body>
</html>
