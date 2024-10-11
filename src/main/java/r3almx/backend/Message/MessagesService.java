package r3almx.backend.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagesService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveMessage(){}
}
