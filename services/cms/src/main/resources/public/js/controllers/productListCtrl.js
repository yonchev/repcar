angular.module('app').controller(
        'productListCtrl',
        [
                '$rootScope',
                '$scope',
                '$log',
                '$timeout',
                '$stateParams',
                'GeneralService',
                function($rootScope, $scope, $log, $timeout, $stateParams, GeneralService) {

                    $scope.currentPage = 0;
                    $scope.sortProductByField = '';
                    $scope.numPerPage = 20;
                    $scope.totalPages = 0;
                    $scope.firstResult = 0;
                    $scope.lastResult = 0;

                    function getUrl(){
                        return 'product/products?companyId=' + $scope.companyId + "&size="
                            + $scope.numPerPage + "&page=" + $scope.currentPage + "&sort=" + $scope.sortProductByField;
                    }

                    $scope.numbersPerPage = function(num) {
                        $scope.numPerPage = num;
                        init();
                    };

                    $scope.sortBy = function(sort) {
                        $log.info("SORT : " + sort);
                        $scope.sortProductByField = sort;
                        init();
                    };

                    $scope.goToNextPage = function() {
                        if ($scope.totalPages == $scope.currentPage + 1) {
                            //TODO Make button inactive when page is with totalPage-1 size;
                            $log.info("You are already at the last page.");
                        } else {
                            $scope.currentPage += 1;
                            GeneralService.get(getUrl()).then(function(response) {
                                $scope.totalPages = response.page.totalPages;
                                $scope.currentPage = response.page.number;
                                $scope.pageSize = response.page.size;
                                $scope.pageTotalElements = response.page.totalElements;
                                $scope.pageTotalPages = response.page.totalPages;
                                $scope.products = response._embedded.products;
                                calculatePageItems();
                            });
                        }
                    };

                    $scope.goToPreviousPage = function() {
                        if ($scope.currentPage == 0) {
                            //TODO Make button inactive when page is with totalPage-1 size;
                            $log.info("You are already at the last page.");
                        } else {
                            $scope.currentPage -= 1;
                            GeneralService.get(getUrl()).then(function(response) {
                                $scope.totalPages = response.page.totalPages;
                                $scope.currentPage = response.page.number;
                                $scope.pageSize = response.page.size;
                                $scope.pageTotalElements = response.page.totalElements;
                                $scope.pageTotalPages = response.page.totalPages;
                                $scope.products = response._embedded.products;
                                calculatePageItems();
                            });
                        }
                    };

                    $scope.goToFirstPage = function() {
                        $scope.currentPage = 0;
                        GeneralService.get(getUrl()).then(function(response) {
                            $scope.products = response._embedded.products;
                        });
                    };

                    $scope.goToLastPage = function() {
                        $scope.currentPage = $scope.totalPages - 1;
                        GeneralService.get(getUrl()).then(function(response) {
                            $scope.currentPage = response.page.number;
                            $scope.products = response._embedded.products;
                        });
                    };

                    $scope.sortProducts = function(sortProductBy) {
                        $scope.sortProductByField = sortProductBy;
                    };

                    calculatePageItems = function() {
                        $scope.lastResult = $scope.pageSize * ($scope.currentPage + 1);
                        $scope.firstResult = 1 + ($scope.currentPage * $scope.pageSize);
                        if ($scope.lastResult > $scope.pageTotalElements) {
                            $scope.lastResult = $scope.pageTotalElements;
                        }
                    }

                    function init() {
                        GeneralService.get(getUrl()).then(
                                function(response) {
                                    if (typeof response != 'undefined' && response != null
                                            && typeof response._embedded.products != 'undefined'
                                            && response._embedded.products != null) {
                                        $scope.products = response._embedded.products;
                                        $scope.totalPages = response.page.totalPages;
                                        $scope.currentPage = response.page.number;
                                        $scope.pageSize = response.page.size;
                                        $scope.pageTotalElements = response.page.totalElements;
                                        $scope.pageTotalPages = response.page.totalPages;
                                        calculatePageItems();
                                    } else {
                                        $scope.products = '{}';
                                    }
                                });
                    }
                    init();
                } ]);
