#
# Autogenerated by Thrift Compiler (0.14.1)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
#  options string: py
#

from thrift.Thrift import TType, TMessageType, TFrozenDict, TException, TApplicationException
from thrift.protocol.TProtocol import TProtocolException
from thrift.TRecursive import fix_spec

import sys
import logging
from .ttypes import *
from thrift.Thrift import TProcessor
from thrift.transport import TTransport
all_structs = []


class Iface(object):
    def logIn(self, userName, key):
        """
        Parameters:
         - userName
         - key

        """
        pass

    def logOut(self):
        pass


class Client(Iface):
    def __init__(self, iprot, oprot=None):
        self._iprot = self._oprot = iprot
        if oprot is not None:
            self._oprot = oprot
        self._seqid = 0

    def logIn(self, userName, key):
        """
        Parameters:
         - userName
         - key

        """
        self.send_logIn(userName, key)
        self.recv_logIn()

    def send_logIn(self, userName, key):
        self._oprot.writeMessageBegin('logIn', TMessageType.CALL, self._seqid)
        args = logIn_args()
        args.userName = userName
        args.key = key
        args.write(self._oprot)
        self._oprot.writeMessageEnd()
        self._oprot.trans.flush()

    def recv_logIn(self):
        iprot = self._iprot
        (fname, mtype, rseqid) = iprot.readMessageBegin()
        if mtype == TMessageType.EXCEPTION:
            x = TApplicationException()
            x.read(iprot)
            iprot.readMessageEnd()
            raise x
        result = logIn_result()
        result.read(iprot)
        iprot.readMessageEnd()
        if result.invalidKeyException is not None:
            raise result.invalidKeyException
        if result.protocolException is not None:
            raise result.protocolException
        return

    def logOut(self):
        self.send_logOut()

    def send_logOut(self):
        self._oprot.writeMessageBegin('logOut', TMessageType.ONEWAY, self._seqid)
        args = logOut_args()
        args.write(self._oprot)
        self._oprot.writeMessageEnd()
        self._oprot.trans.flush()


class Processor(Iface, TProcessor):
    def __init__(self, handler):
        self._handler = handler
        self._processMap = {}
        self._processMap["logIn"] = Processor.process_logIn
        self._processMap["logOut"] = Processor.process_logOut
        self._on_message_begin = None

    def on_message_begin(self, func):
        self._on_message_begin = func

    def process(self, iprot, oprot):
        (name, type, seqid) = iprot.readMessageBegin()
        if self._on_message_begin:
            self._on_message_begin(name, type, seqid)
        if name not in self._processMap:
            iprot.skip(TType.STRUCT)
            iprot.readMessageEnd()
            x = TApplicationException(TApplicationException.UNKNOWN_METHOD, 'Unknown function %s' % (name))
            oprot.writeMessageBegin(name, TMessageType.EXCEPTION, seqid)
            x.write(oprot)
            oprot.writeMessageEnd()
            oprot.trans.flush()
            return
        else:
            self._processMap[name](self, seqid, iprot, oprot)
        return True

    def process_logIn(self, seqid, iprot, oprot):
        args = logIn_args()
        args.read(iprot)
        iprot.readMessageEnd()
        result = logIn_result()
        try:
            self._handler.logIn(args.userName, args.key)
            msg_type = TMessageType.REPLY
        except TTransport.TTransportException:
            raise
        except InvalidKeyException as invalidKeyException:
            msg_type = TMessageType.REPLY
            result.invalidKeyException = invalidKeyException
        except ProtocolException as protocolException:
            msg_type = TMessageType.REPLY
            result.protocolException = protocolException
        except TApplicationException as ex:
            logging.exception('TApplication exception in handler')
            msg_type = TMessageType.EXCEPTION
            result = ex
        except Exception:
            logging.exception('Unexpected exception in handler')
            msg_type = TMessageType.EXCEPTION
            result = TApplicationException(TApplicationException.INTERNAL_ERROR, 'Internal error')
        oprot.writeMessageBegin("logIn", msg_type, seqid)
        result.write(oprot)
        oprot.writeMessageEnd()
        oprot.trans.flush()

    def process_logOut(self, seqid, iprot, oprot):
        args = logOut_args()
        args.read(iprot)
        iprot.readMessageEnd()
        try:
            self._handler.logOut()
        except TTransport.TTransportException:
            raise
        except Exception:
            logging.exception('Exception in oneway handler')

# HELPER FUNCTIONS AND STRUCTURES


class logIn_args(object):
    """
    Attributes:
     - userName
     - key

    """


    def __init__(self, userName=None, key=None,):
        self.userName = userName
        self.key = key

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRING:
                    self.userName = iprot.readString().decode('utf-8', errors='replace') if sys.version_info[0] == 2 else iprot.readString()
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.I32:
                    self.key = iprot.readI32()
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('logIn_args')
        if self.userName is not None:
            oprot.writeFieldBegin('userName', TType.STRING, 1)
            oprot.writeString(self.userName.encode('utf-8') if sys.version_info[0] == 2 else self.userName)
            oprot.writeFieldEnd()
        if self.key is not None:
            oprot.writeFieldBegin('key', TType.I32, 2)
            oprot.writeI32(self.key)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(logIn_args)
logIn_args.thrift_spec = (
    None,  # 0
    (1, TType.STRING, 'userName', 'UTF8', None, ),  # 1
    (2, TType.I32, 'key', None, None, ),  # 2
)


class logIn_result(object):
    """
    Attributes:
     - invalidKeyException
     - protocolException

    """


    def __init__(self, invalidKeyException=None, protocolException=None,):
        self.invalidKeyException = invalidKeyException
        self.protocolException = protocolException

    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            if fid == 1:
                if ftype == TType.STRUCT:
                    self.invalidKeyException = InvalidKeyException.read(iprot)
                else:
                    iprot.skip(ftype)
            elif fid == 2:
                if ftype == TType.STRUCT:
                    self.protocolException = ProtocolException.read(iprot)
                else:
                    iprot.skip(ftype)
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('logIn_result')
        if self.invalidKeyException is not None:
            oprot.writeFieldBegin('invalidKeyException', TType.STRUCT, 1)
            self.invalidKeyException.write(oprot)
            oprot.writeFieldEnd()
        if self.protocolException is not None:
            oprot.writeFieldBegin('protocolException', TType.STRUCT, 2)
            self.protocolException.write(oprot)
            oprot.writeFieldEnd()
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(logIn_result)
logIn_result.thrift_spec = (
    None,  # 0
    (1, TType.STRUCT, 'invalidKeyException', [InvalidKeyException, None], None, ),  # 1
    (2, TType.STRUCT, 'protocolException', [ProtocolException, None], None, ),  # 2
)


class logOut_args(object):


    def read(self, iprot):
        if iprot._fast_decode is not None and isinstance(iprot.trans, TTransport.CReadableTransport) and self.thrift_spec is not None:
            iprot._fast_decode(self, iprot, [self.__class__, self.thrift_spec])
            return
        iprot.readStructBegin()
        while True:
            (fname, ftype, fid) = iprot.readFieldBegin()
            if ftype == TType.STOP:
                break
            else:
                iprot.skip(ftype)
            iprot.readFieldEnd()
        iprot.readStructEnd()

    def write(self, oprot):
        if oprot._fast_encode is not None and self.thrift_spec is not None:
            oprot.trans.write(oprot._fast_encode(self, [self.__class__, self.thrift_spec]))
            return
        oprot.writeStructBegin('logOut_args')
        oprot.writeFieldStop()
        oprot.writeStructEnd()

    def validate(self):
        return

    def __repr__(self):
        L = ['%s=%r' % (key, value)
             for key, value in self.__dict__.items()]
        return '%s(%s)' % (self.__class__.__name__, ', '.join(L))

    def __eq__(self, other):
        return isinstance(other, self.__class__) and self.__dict__ == other.__dict__

    def __ne__(self, other):
        return not (self == other)
all_structs.append(logOut_args)
logOut_args.thrift_spec = (
)
fix_spec(all_structs)
del all_structs