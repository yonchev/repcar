/**
 * Created by IMishev on 8/9/2016.
 */
angular.module('app').factory('ActiveUsersService', [ '$resource', '$log', '$q', function($resource, $log, $q) {
    var activeUsersService = {
    get : function(url) {
        var deferred = $q.defer();
        var Object = $resource(url, {},{
            'query' : {
                method : 'GET',
                isArray : true
            }
        });
        Object.query(function(response) {
            $log.info('Active users received');
            deferred.resolve(response);
        }, function(errorMsg) {
            $log.error('Error fetching item: ' + errorMsg.data.message);
            deferred.reject();
        });
        return deferred.promise;
    }};
    return activeUsersService;
}]);