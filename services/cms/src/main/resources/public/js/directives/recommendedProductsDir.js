angular.module('app').directive('recommendedProducts',
        [ 'RecommendedProductsService', function(RecommendedProductsService) {
            return {
                restrict : 'E',
                scope : {
                    user : '@',
                    action : '@'
                },
                templateUrl : '/view/templates/recommendedProductsTempl.html',
                link : function(scope, element, attrs) {
                    // TODO: make functionality for other action types
                    if (scope.action === 'recommended') {
                        var filter = "companyId=" + scope.$parent.$parent.companyId + "&weakId=" + localStorage.getItem('WEAK_ID');
                        var count = "count=" + 4;
                        var url = "/user-data/userdata/recommendation/product?" + filter + "&" +count;
                        RecommendedProductsService.get(url).then(function(response) {
                            if (typeof response != 'undefined' && response != null) {
                                scope.products = response._embedded.productList;
                                scope.hasProducts = true;
                            } else {
                                scope.hasProducts = false;
                            }
                        });
                    }
                }
            }
        } ]);
