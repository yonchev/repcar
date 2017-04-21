angular.module('app').factory('AuthService', ['$resource', '$http', '$q', '$log', function ($resource, $http, $q, $log) {
    return {
        login: function (request) {
            var deferred = $q.defer();
            $http(request).success(function (response) {
                if (response) {
                    $log.info('User login successfully');
                    deferred.resolve(response);
                } else {
                    $log.info("Error login user " + response);
                    deferred.reject();
                }
            }).error(function (error) {
                $log.info("Error login user " + error);
                deferred.resolve(error);
            });
            return deferred.promise;
        },
        logout: function () {
            var deferred = $q.defer();
            sessionStorage.removeItem('access_token');
            sessionStorage.removeItem('sessionUser');
            $http.defaults.headers.common['Authorization'] = '';
            deferred.resolve();
            return deferred.promise;
        }
    };
}]);
