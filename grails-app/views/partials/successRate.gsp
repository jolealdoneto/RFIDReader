<h2>SuccessRate test</h2>

<p class="btn-toolbar" ng-repeat="tag in tagService.tagList">
    <button class="btn btn-success" ng-click="performTest(tag.tagID)">{{tag.tagID}}</button>
</p>
<div>{{result.result}}</div>
