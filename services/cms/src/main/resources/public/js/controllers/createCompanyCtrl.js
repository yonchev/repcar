angular.module('app').controller('createCompanyCtrl',
        [ '$scope', '$stateParams', 'GeneralService', function($scope, $stateParams, GeneralService) {

            var snackbarContainer = document.querySelector('#demo-snackbar-example');

            var showSnack = function() {
                var data = {
                        message: 'Company successfully created!',
                        timeout: 2000,
                        actionHandler: '',
                        actionText: ''
                      };
                snackbarContainer.MaterialSnackbar.showSnackbar(data);
            }

            $scope.createCompany = function() {

                var data = {
                    "companyName" : $scope.createCompanyName,
                    "companyAddress" : $scope.createCompanyAddress,
                };

                var url = 'company/companies';
                console.log(data);
                GeneralService.save(data, url).then(function(response) {
                    $scope.createCompanyName = "";
                    $scope.createCompanyAddress = "";
                    showSnack();
                });
        };    

        } ]);
