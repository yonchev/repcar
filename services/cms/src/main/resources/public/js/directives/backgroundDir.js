angular.module('app.directives.backgroundDir', []).directive('backgroundDir', function() {
    return {
        restrict : 'A',
        scope : {
            link : '@'
        },
        link : function(scope, element, attrs, timeout) {
            if (attrs.link) {
                element.css("background-image", "url(" + attrs.link + ")")
            }
            scope.$watch('link', function(value, oldValue) {
                if (attrs.link) {
                    element.css("background-image", "url(" + attrs.link + ")")
                }
            });
        }
    }
});
