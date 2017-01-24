/**
 * 
 */
package eu.verdelhan.ta4j.indicators.simple;

import eu.verdelhan.ta4j.Decimal;

/**
 * @author mransley
 *
 */
public class DecimalConstantIndicator extends ConstantIndicator<Decimal> {

    private static final long serialVersionUID = 3236117230103170698L;

    public DecimalConstantIndicator(Decimal t) {
        super(t);
    }

}
