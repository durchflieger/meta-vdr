PR = "r1"
MAINTAINER = "df"
SECTION = "multimedia"

SUMMARY = "OE System utilities for Video Disk Recorder"

DESCRIPTION = "OE system utilities for VDR is a collection of utils to run VDR instead of enigma frontend"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=892f569a555ba9c07a568a7c0c4fa63a"

SRC_URI = "file://S50.check-network-connections \
  file://S95.set-wakeup-alarm \
  file://vdr-oe-utils.sysvinit \
  file://vdr-oe-utils \
  file://COPYING \
"

S = "${WORKDIR}"

inherit update-rc.d allarch

RDEPENDS_${PN} = "vdr stb-hwclock"

PACKAGES = "${PN}"

FILES:${PN} =+ "${datadir}/vdr"

CONFFILES:${PN} = "${sysconfdir}/vdr/vdr-oe-utils \
  ${sysconfdir}/vdr/shutdown-hooks/S50.check-network-connections \
  ${sysconfdir}/vdr/shutdown-hooks/S95.set-wakeup-alarm \
"

INITSCRIPT_NAME = "vdr-oe-utils"
INITSCRIPT_PARAMS = "start 70 5 . stop 70 0 6 ."

do_compile () {
}

do_install () {
  install -D -m 0755 ${WORKDIR}/vdr-oe-utils.sysvinit ${D}${sysconfdir}/init.d/vdr-oe-utils
  install -D -m 0755 -t ${D}${sysconfdir}/vdr ${WORKDIR}/vdr-oe-utils

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/shutdown-hooks ${WORKDIR}/S50.check-network-connections
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/shutdown-hooks ${WORKDIR}/S95.set-wakeup-alarm

  install -d -m 0755 ${D}${datadir}/vdr/shutdown-hooks
  ln -s ${sysconfdir}/vdr/shutdown-hooks/S50.check-network-connections ${D}${datadir}/vdr/shutdown-hooks
  ln -s ${sysconfdir}/vdr/shutdown-hooks/S95.set-wakeup-alarm ${D}${datadir}/vdr/shutdown-hooks
}

pkg_postinst:${PN} () {
  [ -e /etc/inittab ] && sed -e 's/id:3:initdefault:/id:5:initdefault:/' -i /etc/inittab
}

pkg_postrm:${PN} () {
  [ -e /etc/inittab ] && sed -e 's/id:5:initdefault:/id:3:initdefault:/' -i /etc/inittab
}
