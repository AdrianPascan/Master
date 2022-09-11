/**
 * Autogenerated by Thrift Compiler (0.14.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef Login_H
#define Login_H

#include <thrift/TDispatchProcessor.h>
#include <thrift/async/TConcurrentClientSyncInfo.h>
#include <memory>
#include "Task_types.h"

namespace Task2 {

#ifdef _MSC_VER
  #pragma warning( push )
  #pragma warning (disable : 4250 ) //inheriting methods via dominance 
#endif

class LoginIf {
 public:
  virtual ~LoginIf() {}
  virtual void logIn(const std::string& userName, const int32_t key) = 0;
  virtual void logOut() = 0;
};

class LoginIfFactory {
 public:
  typedef LoginIf Handler;

  virtual ~LoginIfFactory() {}

  virtual LoginIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) = 0;
  virtual void releaseHandler(LoginIf* /* handler */) = 0;
};

class LoginIfSingletonFactory : virtual public LoginIfFactory {
 public:
  LoginIfSingletonFactory(const ::std::shared_ptr<LoginIf>& iface) : iface_(iface) {}
  virtual ~LoginIfSingletonFactory() {}

  virtual LoginIf* getHandler(const ::apache::thrift::TConnectionInfo&) {
    return iface_.get();
  }
  virtual void releaseHandler(LoginIf* /* handler */) {}

 protected:
  ::std::shared_ptr<LoginIf> iface_;
};

class LoginNull : virtual public LoginIf {
 public:
  virtual ~LoginNull() {}
  void logIn(const std::string& /* userName */, const int32_t /* key */) {
    return;
  }
  void logOut() {
    return;
  }
};

typedef struct _Login_logIn_args__isset {
  _Login_logIn_args__isset() : userName(false), key(false) {}
  bool userName :1;
  bool key :1;
} _Login_logIn_args__isset;

class Login_logIn_args {
 public:

  Login_logIn_args(const Login_logIn_args&);
  Login_logIn_args& operator=(const Login_logIn_args&);
  Login_logIn_args() : userName(), key(0) {
  }

  virtual ~Login_logIn_args() noexcept;
  std::string userName;
  int32_t key;

  _Login_logIn_args__isset __isset;

  void __set_userName(const std::string& val);

  void __set_key(const int32_t val);

  bool operator == (const Login_logIn_args & rhs) const
  {
    if (!(userName == rhs.userName))
      return false;
    if (!(key == rhs.key))
      return false;
    return true;
  }
  bool operator != (const Login_logIn_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const Login_logIn_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class Login_logIn_pargs {
 public:


  virtual ~Login_logIn_pargs() noexcept;
  const std::string* userName;
  const int32_t* key;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _Login_logIn_result__isset {
  _Login_logIn_result__isset() : invalidKeyException(false), protocolException(false) {}
  bool invalidKeyException :1;
  bool protocolException :1;
} _Login_logIn_result__isset;

class Login_logIn_result {
 public:

  Login_logIn_result(const Login_logIn_result&);
  Login_logIn_result& operator=(const Login_logIn_result&);
  Login_logIn_result() {
  }

  virtual ~Login_logIn_result() noexcept;
  InvalidKeyException invalidKeyException;
  ProtocolException protocolException;

  _Login_logIn_result__isset __isset;

  void __set_invalidKeyException(const InvalidKeyException& val);

  void __set_protocolException(const ProtocolException& val);

  bool operator == (const Login_logIn_result & rhs) const
  {
    if (!(invalidKeyException == rhs.invalidKeyException))
      return false;
    if (!(protocolException == rhs.protocolException))
      return false;
    return true;
  }
  bool operator != (const Login_logIn_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const Login_logIn_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _Login_logIn_presult__isset {
  _Login_logIn_presult__isset() : invalidKeyException(false), protocolException(false) {}
  bool invalidKeyException :1;
  bool protocolException :1;
} _Login_logIn_presult__isset;

class Login_logIn_presult {
 public:


  virtual ~Login_logIn_presult() noexcept;
  InvalidKeyException invalidKeyException;
  ProtocolException protocolException;

  _Login_logIn_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class Login_logOut_args {
 public:

  Login_logOut_args(const Login_logOut_args&);
  Login_logOut_args& operator=(const Login_logOut_args&);
  Login_logOut_args() {
  }

  virtual ~Login_logOut_args() noexcept;

  bool operator == (const Login_logOut_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const Login_logOut_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const Login_logOut_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class Login_logOut_pargs {
 public:


  virtual ~Login_logOut_pargs() noexcept;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

class LoginClient : virtual public LoginIf {
 public:
  LoginClient(std::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) {
    setProtocol(prot);
  }
  LoginClient(std::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, std::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) {
    setProtocol(iprot,oprot);
  }
 private:
  void setProtocol(std::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) {
  setProtocol(prot,prot);
  }
  void setProtocol(std::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, std::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) {
    piprot_=iprot;
    poprot_=oprot;
    iprot_ = iprot.get();
    oprot_ = oprot.get();
  }
 public:
  std::shared_ptr< ::apache::thrift::protocol::TProtocol> getInputProtocol() {
    return piprot_;
  }
  std::shared_ptr< ::apache::thrift::protocol::TProtocol> getOutputProtocol() {
    return poprot_;
  }
  void logIn(const std::string& userName, const int32_t key);
  void send_logIn(const std::string& userName, const int32_t key);
  void recv_logIn();
  void logOut();
  void send_logOut();
 protected:
  std::shared_ptr< ::apache::thrift::protocol::TProtocol> piprot_;
  std::shared_ptr< ::apache::thrift::protocol::TProtocol> poprot_;
  ::apache::thrift::protocol::TProtocol* iprot_;
  ::apache::thrift::protocol::TProtocol* oprot_;
};

class LoginProcessor : public ::apache::thrift::TDispatchProcessor {
 protected:
  ::std::shared_ptr<LoginIf> iface_;
  virtual bool dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext);
 private:
  typedef  void (LoginProcessor::*ProcessFunction)(int32_t, ::apache::thrift::protocol::TProtocol*, ::apache::thrift::protocol::TProtocol*, void*);
  typedef std::map<std::string, ProcessFunction> ProcessMap;
  ProcessMap processMap_;
  void process_logIn(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_logOut(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
 public:
  LoginProcessor(::std::shared_ptr<LoginIf> iface) :
    iface_(iface) {
    processMap_["logIn"] = &LoginProcessor::process_logIn;
    processMap_["logOut"] = &LoginProcessor::process_logOut;
  }

  virtual ~LoginProcessor() {}
};

class LoginProcessorFactory : public ::apache::thrift::TProcessorFactory {
 public:
  LoginProcessorFactory(const ::std::shared_ptr< LoginIfFactory >& handlerFactory) :
      handlerFactory_(handlerFactory) {}

  ::std::shared_ptr< ::apache::thrift::TProcessor > getProcessor(const ::apache::thrift::TConnectionInfo& connInfo);

 protected:
  ::std::shared_ptr< LoginIfFactory > handlerFactory_;
};

class LoginMultiface : virtual public LoginIf {
 public:
  LoginMultiface(std::vector<std::shared_ptr<LoginIf> >& ifaces) : ifaces_(ifaces) {
  }
  virtual ~LoginMultiface() {}
 protected:
  std::vector<std::shared_ptr<LoginIf> > ifaces_;
  LoginMultiface() {}
  void add(::std::shared_ptr<LoginIf> iface) {
    ifaces_.push_back(iface);
  }
 public:
  void logIn(const std::string& userName, const int32_t key) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->logIn(userName, key);
    }
    ifaces_[i]->logIn(userName, key);
  }

  void logOut() {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->logOut();
    }
    ifaces_[i]->logOut();
  }

};

// The 'concurrent' client is a thread safe client that correctly handles
// out of order responses.  It is slower than the regular client, so should
// only be used when you need to share a connection among multiple threads
class LoginConcurrentClient : virtual public LoginIf {
 public:
  LoginConcurrentClient(std::shared_ptr< ::apache::thrift::protocol::TProtocol> prot, std::shared_ptr<::apache::thrift::async::TConcurrentClientSyncInfo> sync) : sync_(sync)
{
    setProtocol(prot);
  }
  LoginConcurrentClient(std::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, std::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot, std::shared_ptr<::apache::thrift::async::TConcurrentClientSyncInfo> sync) : sync_(sync)
{
    setProtocol(iprot,oprot);
  }
 private:
  void setProtocol(std::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) {
  setProtocol(prot,prot);
  }
  void setProtocol(std::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, std::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) {
    piprot_=iprot;
    poprot_=oprot;
    iprot_ = iprot.get();
    oprot_ = oprot.get();
  }
 public:
  std::shared_ptr< ::apache::thrift::protocol::TProtocol> getInputProtocol() {
    return piprot_;
  }
  std::shared_ptr< ::apache::thrift::protocol::TProtocol> getOutputProtocol() {
    return poprot_;
  }
  void logIn(const std::string& userName, const int32_t key);
  int32_t send_logIn(const std::string& userName, const int32_t key);
  void recv_logIn(const int32_t seqid);
  void logOut();
  void send_logOut();
 protected:
  std::shared_ptr< ::apache::thrift::protocol::TProtocol> piprot_;
  std::shared_ptr< ::apache::thrift::protocol::TProtocol> poprot_;
  ::apache::thrift::protocol::TProtocol* iprot_;
  ::apache::thrift::protocol::TProtocol* oprot_;
  std::shared_ptr<::apache::thrift::async::TConcurrentClientSyncInfo> sync_;
};

#ifdef _MSC_VER
  #pragma warning( pop )
#endif

} // namespace

#endif