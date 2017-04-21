angular.module('app').controller('createProductCtrl',
        [ '$scope', '$stateParams', 'GeneralService', function($scope, $stateParams, GeneralService) {

            var snackbarContainer = document.querySelector('#demo-snackbar-example');

            var showSnack = function(text) {
                var data = {
                        message: text,
                        timeout: 2000,
                        actionHandler: '',
                        actionText: ''
                      };
                      snackbarContainer.MaterialSnackbar.showSnackbar(data);
            }

            var url = 'category/categories?companyId=';
            url = url.concat($scope.companyId);
            GeneralService.get(url).then(
                    function(response) {
                        $scope.categories = response._embedded.categories;
                    }, function(error) {
                        if(error.status === 500){
                           showSnack("Cannot get list of categories.");
                        }
                    });
            $scope.selectedCategoriesString = '';
            $scope.selectedCategories = [];
            $scope.setValue = function(mode) {
                $scope.selectedCategories.push(mode);
                if($scope.selectedCategoriesString===''){
                    $scope.selectedCategoriesString = $scope.selectedCategoriesString.concat(mode.categoryName);
                }else{
                    $scope.selectedCategoriesString = $scope.selectedCategoriesString.concat(',',mode.categoryName);
                }
                $(".mdl-menu__container").removeClass("is-visible");
                $scope.selectedCategory = "";
            }
            $scope.createProduct = function() {
                var categoriesIds = [];
                for(var i=0 ;i<$scope.selectedCategories.length;i++){
                    var categoryId = {
                            "categoryId": $scope.selectedCategories[i].categoryId
                    }
                    categoriesIds.push(categoryId);
                }
                var data = {
                    "productName" : $scope.createProductName,
                    "productPrice" : $scope.createProductPrice,
                    "productImage" : $scope.createProductImage,
                    "productDescription" : $scope.createProductDescription,
                    "productRfid" : $scope.createProductRfid,
                    "productNfc" : $scope.createProductNfc,
                    "productCategories" : categoriesIds,
                    "company" : {
                        "companyId" : $scope.companyId //TODO: should be fetched from the user eg. $scope.user.companyId
                    }
                };

                var urlSave = 'product/products';

                GeneralService.save(data, urlSave).then(function(response) {
                    var createdGroupChatName = response;

                    $scope.createProductName = "";
                    $scope.createProductPrice = "";
                    $scope.createProductImage = "";
                    $scope.createProductDescription = "";
                    $scope.createProductRfid = "";
                    $scope.createProductNfc = "";
                    $scope.selectedCategoriesString = '';
                    $scope.selectedCategories = [];

                    showSnack("Product created successfull!");
                });
            };

        } ]);
