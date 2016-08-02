package ywcai.ls.desk.manage;
import java.util.List;
import org.apache.mina.core.session.IoSession;

public interface SessionManageInf{
public void addSession(SessionAssist sAssist,IoSession session);
public void removeSession(SessionAssist sAssist,IoSession session);
public List<IoSession> getSessionList(String username);
}
