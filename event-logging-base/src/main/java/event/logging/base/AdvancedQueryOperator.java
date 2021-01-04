package event.logging.base;

import java.util.List;

/**
 * Marker interface for {@link AdvancedQueryOperator} implementations,
 * e.g. Not, And, Or etc.
 */
public interface AdvancedQueryOperator extends AdvancedQueryItem {

    List<AdvancedQueryItem> getQueryItems();
}
