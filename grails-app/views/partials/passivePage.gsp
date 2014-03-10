<h2>Passive Grid page</h2>

<table class="table table-striped">
    <tr>
        <th>ID</th>
        <th>Antena</th>
        <th>Velocidade</th>
    </tr>
    <tr ng-repeat="tag in tagService.tagList">
        <td>{{tag.tagID}}</td>
        <td>{{tag.antenna}}</td>
        <td>{{tag.smoothSpeed}}</td>
    </tr>
</table>
