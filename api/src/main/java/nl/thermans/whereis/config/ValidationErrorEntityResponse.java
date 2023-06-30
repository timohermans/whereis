package nl.thermans.whereis.config;

import java.util.List;

public record ValidationErrorEntityResponse(List<ValidationError> errors) {
}
