package ywcai.ls.desk.manage;
import java.util.List;
import org.apache.mina.core.session.IoSession;

public interface SessionManageInf{
public void addSession(String userame,IoSession session);
public void removeSession(String username,IoSession session);
public List<IoSession> getSessionList(String username);
}
