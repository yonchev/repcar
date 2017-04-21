/**
 * Created by IMishev on 6/30/2016.
 */
angular.module('app').controller(
    'logoutCtrl',
    ['$rootScope', '$scope', '$resource', '$http', '$q', '$log', 'AuthService', '$document',
        function ($rootScope, $scope, $resource, $http, $q, $log, AuthService, $document) {
            AuthService.logout().then(function() {
                //TODO do not clear localStorage for production
                localStorage.clear();
                $scope.logoutButton = false;
                window.location.href = "/";
            });
        }]);