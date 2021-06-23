package com.barshid.schematech.service;

import com.barshid.schematech.model.bo.User;
import com.barshid.schematech.model.bo.UserGroup;
import com.barshid.schematech.model.repository.UserGroupRepository;
import com.barshid.schematech.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class DataUpdate {

    UserRepository userRepository;
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    UserGroupRepository userGroupRepository;
    @Autowired
    public void setUserGroupRepository(UserGroupRepository userGroupRepository) {
        this.userGroupRepository = userGroupRepository;
    }


    NativeSqlService nativeSqlService;
    @Autowired
    public void setNativeRepository(NativeSqlService nativeSqlService) {
        this.nativeSqlService = nativeSqlService;
    }

    public Boolean updateDataVersion(){
        log.info("start Update Data");
        if (userRepository.count()<=0){
            //Creating Admin.
            User u =new User();
            u.setEmail("a@b.com");
            u.setCountryCode(1);
            u.setPhoneNumber(11111111L);
            u.setPassword("$2a$10$gEO0VAGR6PVrUrS0iH.d8.O6OOBZx/K8n812Wa1fubZl4YvdYxVxi");/* pass:secret*/
            u.setEnabled(true);

            UserGroup ug = new UserGroup();
            ug.setGroupName("admingroup1");
//            ug.getUsers().add(u);
            userGroupRepository.save(ug);
//            Authority role_admin = new Authority("ROLE_ADMIN", ug);
//            authorityRepository.save(role_admin);
            u.getGroups().add(ug);
            userRepository.save(u);

            //Creating Basic User.
            u =new User();
            u.setEmail("b@b.com");
            u.setCountryCode(98);
            u.setPhoneNumber(9131535774L);
            u.setPassword("$2a$10$gEO0VAGR6PVrUrS0iH.d8.O6OOBZx/K8n812Wa1fubZl4YvdYxVxi");/* pass:secret*/
            u.setEnabled(true);

            ug = new UserGroup();
            ug.setGroupName("usergroup1");
//            ug.getUsers().add(u);
            userGroupRepository.save(ug);
//            Authority role_user = new Authority("ROLE_USER", ug);
//            authorityRepository.save(role_user);
            u.getGroups().add(ug);
            userRepository.save(u);

//            nativeRepository.executeSQL("create table if not exists oauth_client_details (" +
//                    "    client_id varchar(256) primary key," +
//                    "    resource_ids varchar(256)," +
//                    "    client_secret varchar(256)," +
//                    "    scope varchar(256)," +
//                    "    authorized_grant_types varchar(256)," +
//                    "    web_server_redirect_uri varchar(256)," +
//                    "    authorities varchar(256)," +
//                    "    access_token_validity integer," +
//                    "    refresh_token_validity integer," +
//                    "    additional_information varchar(4096)," +
//                    "    autoapprove varchar(256)" +
//                    ");");
            nativeSqlService.executeSQL("create table if not exists oauth_access_token (" +
                    "token_id VARCHAR(255)," +
                    "token LONG VARBINARY," +
                    "authentication_id VARCHAR(255) PRIMARY KEY," +
                    "user_name VARCHAR(255)," +
                    "client_id VARCHAR(255)," +
                    "authentication LONG VARBINARY," +
                    "refresh_token VARCHAR(255)" +
                    ");");
            nativeSqlService.executeSQL("create table if not exists oauth_refresh_token (" +
                    "                                                   token_id VARCHAR(255)," +
                    "                                                   token LONG VARBINARY," +
                    "                                                   authentication LONG VARBINARY" +
                    ");");
            //todo test please.
            nativeSqlService.executeSQL("INSERT INTO group_authorities (authority, group_id) VALUES ('ROLE_ADMIN', 1);");
            nativeSqlService.executeSQL("INSERT INTO group_authorities (authority, group_id) VALUES ('ROLE_USER', 2);");

        }

        return true;
    }


}
