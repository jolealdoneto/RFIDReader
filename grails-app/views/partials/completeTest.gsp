<h2>ReadRate test</h2>

<p class="btn-toolbar" ng-repeat="tag in tagService.tagList">
    <button class="btn btn-success" ng-click="performTest(tag.tagID)">{{tag.tagID}}</button>
</p>

<form role="form" class="form-inline">
    <div class="form-group">
        <label for="angle">Ângulo</label>
        <input name="angle" type="text" ng-model="angle" />
    </div>
    <div class="form-group">
        <label for="distance">Distância</label>
        <input name="distance" type="text" ng-model="distance" />
    </div>
</form>

<table class="table table-bordered">
    <tr>
        <th>#</th>
        <th>TagID</th>
        <th>Ângulo</th>
        <th>Distância</th>
        <th>ReadRate</th>
        <th>Successrate</th>
        <th></th>
    </tr>
    <tr ng-repeat="row in results">
        <td>{{$index}}</td>
        <td>{{row.tagId}}</td>
        <td>{{row.angle}}</td>
        <td>{{row.distance}}</td>
        <td>{{row.readrate}}</td>
        <td>{{row.successrate}}</td>
        <td><button class="btn btn-warning" ng-click="deleteRow($index)">deletar</button></td>
    </tr>
</table>
