angular.module('ativ3', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'partials/',
        controller: 'index'
      }).
      when('/passivePage', {
        templateUrl: 'partials/passivePage',
        controller: 'PassivePage'
      }).
      when('/readRate', {
        templateUrl: 'partials/readRate',
        controller: 'ReadRate'
      }).
      when('/successRate', {
        templateUrl: 'partials/successRate',
        controller: 'SuccessRate'
      }).
      when('/completeTest', {
        templateUrl: 'partials/completeTest',
        controller: 'CompleteTest'
      }).
      otherwise({
        redirectTo: '/'
      });
}])

.controller('index', ['$scope', '$location', function($scope, $location) {
    $scope.readRateTest = function() {
        $location.path('/readRate');
    };
    $scope.successRateTest = function() {
        $location.path('/successRate');
    };
    $scope.passivePage = function() {
        $location.path('/passivePage');
    };
    $scope.completeTestPage = function() {
        $location.path('/completeTest');
    };
}])
.controller('CompleteTest', ['$scope', 'TagService', function($scope, TagService) {
    $scope.tagService = TagService;
    $scope.results = [];
    $scope.performTest = function(tagId) {
        TagService.completeTest(tagId).then(function(data) {
            $scope.results.push({
                tagId: tagId,
                angle: $scope.angle,
                distance: $scope.distance,
                readrate: data.data.result.readRate,
                successrate: data.data.result.successRate
            });
        });
    };
    $scope.deleteRow = function(index) {
        $scope.results.splice(index, 1);
    };
    
    // Fetch all tags first
    TagService.readTags();
}])
.controller('ReadRate', ['$scope', 'TagService', function($scope, TagService) {
    $scope.tagService = TagService;
    $scope.performTest = function() {
        TagService.readRate();
    };
}])
.controller('SuccessRate', ['$scope', 'TagService', function($scope, TagService) {
    $scope.tagService = TagService;
    $scope.result = {};
    $scope.performTest = function(tagID) {
        TagService.successRate(tagID).then(function(tag) {
            $scope.result = tag.data;
        });
    };

    // Fetch all tags first
    TagService.readTags();
}])
.controller('PassivePage', ['$scope', 'TagService', function($scope, TagService) {
    $scope.tagService = TagService;
    window.setInterval(function() {
        TagService.readTags();
    }, 500);
}])

.service('TagService', ['FetchResource', function(FetchResource) {
    function TagService() {
        var self = this;
        this.tagList = [];

        this.readRate = function(tagId) {
            console.log('js: ' + tagId);
            FetchResource.fetch('reading/readRate', { tagId: tagId }).success(function(data, status) {
                self.tagList = data.slice();
            });
        };
        this.successRate = function(tagId) {
            return FetchResource.fetch('reading/successRate', {tagId: tagId}).success(function(data, status) {
                return data;
            });
        };
        this.readTags = function() {
            FetchResource.fetch('reading/readTags', {}).success(function(data, status) {
                self.tagList = data.slice();
            });
        };
        this.completeTest = function(tagId) {
            return FetchResource.fetch('reading/completeTest', {tagId: tagId}).success(function(data, status) {
                return data;
            });
        };
    }

    return new TagService();
}])
.service('FetchResource', ['$http', function($http) {
    function FetchResource() {
        var self = this;

        function transformToQueryString(data) {
            var query = [];
            angular.forEach(data, function(v,k) {
                query.push(k+'='+v);
            });

            return '?' + query.join('&');
        }
        this.fetch = function(url, data) {
            console.log(transformToQueryString(data));
            return $http({method: 'GET', url: url + transformToQueryString(data)});
        };

    }


    return new FetchResource();
}])

;
