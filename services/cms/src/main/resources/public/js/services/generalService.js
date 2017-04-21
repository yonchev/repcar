/*
 * Copyright RepCar AD 2017
 */
angular.module('app').factory('GeneralService', [ '$resource', '$log', '$q', '$http', function($resource, $log, $q, $http) {
    var GeneralService = {
        save : function(object, url) {
            var deferred = $q.defer();
            $resource(url,{},{
                save:{
                    method:'POST',
                    headers:{
                        'Content-Type': 'application/hal+json'
                    }
                }
              }).save(object, function(response) {
                $log.info('Item saved');
                deferred.resolve(response);
            }, function(errorMsg) {
                $log.error('Error creating item in '+ errorMsg.status);
                if(errorMsg.data.error === "invalid_token"){
                    sessionStorage.removeItem('access_token');
                    sessionStorage.removeItem('sessionUser');
                    $http.defaults.headers.common['Authorization'] = '';
                    window.location.href = "/";
                }
                deferred.reject(errorMsg);
            });
            return deferred.promise;
        },
        get : function(url) {
            var deferred = $q.defer();
            $resource(url).get(function(response) {
                $log.info('Item Received');
                deferred.resolve(response);
            }, function(errorMsg) {
                $log.error('Error fetching item ' + errorMsg.status);
                if(errorMsg.data.error === "invalid_token"){
                    sessionStorage.removeItem('access_token');
                    sessionStorage.removeItem('sessionUser');
                    $http.defaults.headers.common['Authorization'] = '';
                    window.location.href = "/";
                }
                deferred.reject(errorMsg);
            });
            return deferred.promise;
        }
    };
    return GeneralService;
} ]);
