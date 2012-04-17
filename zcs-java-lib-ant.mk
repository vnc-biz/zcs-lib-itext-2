
include $(TOPDIR)/conf.mk

ANT?=ant

ZIMLET_USER_JARDIR=mailboxd/webapps/zimbra/WEB-INF/lib
ZIMLET_ADMIN_JARDIR=mailboxd/webapps/zimbraAdmin/WEB-INF/lib
ZIMLET_SERVICE_JARDIR=mailboxd/webapps/service/WEB-INF/lib
ZIMLET_LIB_JARDIR=lib/jars

all:	build

build:	check build_ant install_lib install_user install_admin install_service

check:
ifeq ($(BUILD_ANT_SUBDIR),)
	@echo "missing $(BUILD_ANT_SUBDIR)" >&2
	@exit 1
endif

ifeq ($(INSTALL_USER),y)
install_user:	build_ant
	@mkdir -p $(IMAGE_ROOT)/$(ZIMLET_USER_JARDIR)
	@cp $(BUILD_ANT_JARFILE) $(IMAGE_ROOT)/$(ZIMLET_USER_JARDIR)
else
install_user:
	@echo -n
endif

ifeq ($(INSTALL_ADMIN),y)
install_admin:	build_ant
	@mkdir -p $(IMAGE_ROOT)/$(ZIMLET_ADMIN_JARDIR)
	@cp $(BUILD_ANT_JARFILE) $(IMAGE_ROOT)/$(ZIMLET_ADMIN_JARDIR)
else
install_admin:
	@echo -n
endif

ifeq ($(INSTALL_SERVICE),y)
install_service:	build_ant
	@mkdir -p $(IMAGE_ROOT)/$(ZIMLET_SERVICE_JARDIR)
	@cp $(BUILD_ANT_JARFILE) $(IMAGE_ROOT)/$(ZIMLET_SERVICE_JARDIR)
else
install_service:
	@echo -n
endif

ifeq ($(INSTALL_LIB),y)
install_lib:	build_ant
	@mkdir -p $(IMAGE_ROOT)/$(ZIMLET_LIB_JARDIR)
	@cp $(BUILD_ANT_JARFILE) $(IMAGE_ROOT)/$(ZIMLET_LIB_JARDIR)
else
install_lib:
	@echo -n
endif

clean:
	@cd $(BUILD_ANT_SUBDIR) && $(ANT) clean
	@rm -Rf \
		classes		\
		$(IMAGE_ROOT)/$(ZIMLET_SERVICE_JARDIR)/$(BUILD_ANT_JARFILE)	\
		$(IMAGE_ROOT)/$(ZIMLET_ADMIN_JARDIR)/$(BUILD_ANT_JARFILE)	\
		$(IMAGE_ROOT)/$(ZIMLET_USER_JARDIR)/$(BUILD_ANT_JARFILE)

build_ant:
	@cd $(BUILD_ANT_SUBDIR) && $(ANT) $(BUILD_ANT_TARGET)
