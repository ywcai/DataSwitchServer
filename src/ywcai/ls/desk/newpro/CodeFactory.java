package ywcai.ls.desk.newpro;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class CodeFactory  extends DemuxingProtocolCodecFactory {
    public CodeFactory() {
            addMessageDecoder(MesDecode.class);
            addMessageEncoder(MesInf.class, new MesEncode());
    }
	
}
