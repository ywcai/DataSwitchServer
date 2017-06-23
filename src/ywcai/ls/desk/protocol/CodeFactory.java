package ywcai.ls.desk.protocol;


import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class CodeFactory extends DemuxingProtocolCodecFactory {

	public CodeFactory(MesEncode encoder)
	{
		addMessageDecoder(MesDecode.class);
		addMessageEncoder(MesReqInf.class,encoder);
	}
}
