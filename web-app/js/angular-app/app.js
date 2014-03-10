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

        this.readRate = function() {
            FetchResource.fetch('reading/readRate', {}).success(function(data, status) {
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
    }

    return new TagService();
}])
.service('FetchResource', ['$http', function($http) {
    function FetchResource() {
        var self = this;

        this.fetch = function(url, data) {
            return $http({method: 'GET', url: url, data: data});
        };

    }


    return new FetchResource();
}])

;
