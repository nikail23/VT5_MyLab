package horseraceapp.util.dao;

import horseraceapp.util.dao.entity.User;

public interface UserDao {
    boolean registerUser(User user);
    User getUserByEmail(String email);
    User getUserById(Integer userId);
    Integer getUserBalance(Integer userId);
    boolean rechargeBalance(Integer userId, Integer rechargeAmount);
}
