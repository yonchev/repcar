angular.module('app').directive('menuClose', function() {
    return {
        restrict : 'AC',
        link : function($scope, $element) {
            $element.bind('click', function() {
                var drawer = angular.element(document.querySelector('.mdl-layout__drawer'));
                var content = angular.element(document.querySelector('.mdl-layout__obfuscator'));
                if (drawer && content) {
                    drawer.toggleClass('is-visible');
                    content.toggleClass('is-visible');
                }
            });
        }
    };
});