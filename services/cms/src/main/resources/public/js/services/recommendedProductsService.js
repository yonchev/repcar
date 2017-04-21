/**
 * Created by mmanchev on 6/23/2016.
 */
angular.module('app').factory('RecommendedProductsService', [ '$log', '$q', 'GeneralService', function($log, $q, GeneralService) {
    var RecommendedProductsService = {
        get : function(url) {
            var deferred = $q.defer();
            GeneralService.get(url).then(function(response) {
                $log.info('Item Received');
                deferred.resolve(response);
            }, function(errorMsg) {
                $log.error('Error fetching item: ' + errorMsg.data.message);
                deferred.reject();
            });
            return deferred.promise;
        }
    };
    return RecommendedProductsService;
} ]);
