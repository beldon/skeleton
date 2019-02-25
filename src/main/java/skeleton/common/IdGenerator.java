package skeleton.common;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import skeleton.common.util.IdGeneratorUtil;

import java.io.Serializable;

/**
 * @author Beldon
 */
public class IdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return IdGeneratorUtil.getInstance().generateId();
    }
}
