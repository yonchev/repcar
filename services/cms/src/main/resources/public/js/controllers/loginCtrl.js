angular.module('app').controller(
    'loginCtrl',
    ['$rootScope', '$scope', '$resource', '$http', '$q', '$log', 'AuthService', 'UserService',
        function ($rootScope, $scope, $resource, $http, $q, $log, AuthService, UserService) {
            $scope.onEnter = function(keyEvent, username, password) {
                if (keyEvent.which === 13)
                    $scope.login(username, password);
            };

            $scope.login = function (username, password) {
                var req = {
                    method: 'POST',
                    url: '/token?username=' + username + '&password=' + password
                };
                AuthService.login(req).then(function (response) {
                    if (response.access_token) {
                        $http.defaults.headers.common['Authorization'] = 'Bearer ' + response.access_token;
                        sessionStorage.setItem('access_token', response.access_token);
                        $scope.logoutButton = true;
                        UserService.loadUser().then(function() {
                            window.location.href = "/";
                        });
                    } else if(response.error) {
                        alert("Bad Credentials!");
                    }
                });
            }
        }]);