TOPDIR=..

include $(TOPDIR)/conf.mk

all:	build

build:
	@$(MAKE) -C lib-itext

clean:
	@$(MAKE) -C lib-itext clean
	rm -f $(TOPDIR)/lib/iText.jar
