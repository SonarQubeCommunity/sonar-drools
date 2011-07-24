/*
 * Sonar Drools Plugin
 * Copyright (C) 2011 Jérémie Lagarde
 * dev@sonar.codehaus.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonar.api.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Violation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public abstract class MockSensorContext implements SensorContext {

  Multimap<Resource, Measure> measures;
  Map<String, String> sources;
  Collection<Violation> violations;

  private Multimap<Resource, Measure> getMeasures() {
    if (measures == null) {
      measures = ArrayListMultimap.create();
    }
    return measures;
  }

  private Collection<Violation> getViolations() {
    if (violations == null) {
      violations = new ArrayList<Violation>();
    }
    return violations;
  }

  private Map<String,String> getSources() {
    if (sources == null) {
      sources = new HashMap<String,String>();
    }
    return sources;
  }
  
  public Measure saveMeasure(Resource resource, Measure measure) {
    getMeasures().put(resource, measure);
    return measure;
  }

  public Measure saveMeasure(Resource resource, Metric metric, Double value) {
    Measure measure = new Measure(metric, value);
    getMeasures().put(resource, measure);
    return measure;
  }

  public Measure saveMeasure(Measure measure) {
    getMeasures().put(null, measure);
    return measure;
  }

  public Measure saveMeasure(Metric metric, Double value) {
    return saveMeasure(new Measure(metric, value));
  }

  public Measure getMeasure(Resource resource, Metric metric) {
    for (Measure measure : getMeasures().get(resource)) {
      if (measure.getMetric().equals(metric))
        return measure;
    }
    return null;
  }

  public Measure getMeasure(Metric metric) {
    return getMeasure(null, metric);
  }

  public Resource getResource(Resource resource) {
    // TODO check if its a saved resource ...
    return resource;
  }

  public void saveViolations(Collection<Violation> violations) {
    getViolations().addAll(violations);
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(MockSensorContext.class.getCanonicalName()).append("\n");
    builder.append("  Measures ").append("\n");
    if (measures.get(null).size() != 0) {
      builder.append("    project : ");
      for (Measure measure : measures.get(null)) {
        builder.append(measure.toString()).append(" ");   
      }
      builder.append("\n");
    }
    for (Resource resource : measures.keySet()) {
      if (resource!=null) {
        builder.append("    ").append(resource.getName()).append(" ");
        for (Measure measure : measures.get(resource)) {
          builder.append("\"").append(measure.getMetric().getName()).append("\"=").append(measure.getValue()).append(" ");   
        }
        builder.append("\n");
      }
    }
    builder.append("  Violations ").append("\n");
    for (Violation violation : getViolations()) {
      builder.append("    ").append(violation.getResource().getName()).append(" : ").append(violation.getRule().getKey()).append(" ").append(violation.getMessage()).append("\n");
    }    
    return builder.toString();
  } 
  
  public void saveSource(Resource resource, String source) {
    getSources().put(resource.getKey(), source);    
  }
}