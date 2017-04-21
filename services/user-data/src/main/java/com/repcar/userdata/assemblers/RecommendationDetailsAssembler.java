package com.repcar.userdata.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.repcar.userdata.bean.RecommendationDetails;
import com.repcar.userdata.resources.RecommendationDetailsResource;
import com.repcar.userdata.rest.RecommendationDetailsController;

@Service
public class RecommendationDetailsAssembler
        extends ResourceAssemblerSupport<RecommendationDetails, RecommendationDetailsResource> {

    public RecommendationDetailsAssembler() {
        super(RecommendationDetailsController.class, RecommendationDetailsResource.class);
    }

    public RecommendationDetailsResource toResource(RecommendationDetails details) {
        RecommendationDetailsResource detailsResource = new RecommendationDetailsResource(
                details.getRecommendationDetailsId(), details.getApplicationName(), details.getApplicationAccessKey(),
                details.getProductRecommenderUrl(), details.getCategoryRecommenderUrl(),
                details.getCompanyId());
        detailsResource.add(linkTo(RecommendationDetailsController.class)
                .slash(detailsResource.getRecommendationDetailsId()).withSelfRel());
        return detailsResource;

    }
}
