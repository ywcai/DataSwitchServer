package ywcai.ls.desk.protocol;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class CodeFactory  extends DemuxingProtocolCodecFactory {

	private MesDecode decoder;

    private MesEncode encoder;

    public CodeFactory(MesDecode decoder,
    		MesEncode encoder) {
        this.decoder = decoder;
        this.encoder = encoder;
        addMessageDecoder(this.decoder);
        addMessageEncoder(MesReqInf.class, this.encoder);
    }
	
}
