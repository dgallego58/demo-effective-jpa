package com.demo.infrastructure.port.output.data.views;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.demo.infrastructure.port.output.data.Convention;

import java.util.UUID;

@EntityView(Convention.class)
public interface ConventionView {

    @IdMapping
    UUID getId();

    String getLocation();

}
