package skeleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Beldon
 */
@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractTest {
    @Autowired
    protected MockMvc mockMvc;
}
