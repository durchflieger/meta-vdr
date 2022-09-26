PR = "r2"
MAINTAINER = "df"
SECTION = "multimedia"

SUMMARY = "Video Disk Recorder"

DESCRIPTION = "Video Disk Recorder (VDR) is a digital sat-receiver program using \
Linux and DVB technologies. It allows one to record streams, \
as well as output the stream to TV."

HOMEPAGE = "http://www.tvdr.de/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=892f569a555ba9c07a568a7c0c4fa63a"

SRC_URI = "git://git.tvdr.de/vdr.git;protocol=http \
  file://01-dvb-oe.patch \
  file://02-6hour-config-save-interval.patch \
  file://00-vdr.conf \
  file://R90.custom \
  file://README.Debian \
  file://S90.custom \
  file://commands-loader.sh \
  file://commands.custom.conf \
  file://config-loader.sh \
  file://merge-commands.sh \
  file://reccmds.custom.conf \
  file://runvdr \
  file://themes/sttng-blue.theme \
  file://themes/sttng-cool.theme \
  file://vdr-recordingaction \
  file://vdr-shutdown \
  file://vdr.default \
  file://vdr.volatiles \
  file://vdr.sysvinit \
  file://fbc-config \
  file://fbc.conf \
"

SRCREV="${PV}"

S = "${WORKDIR}/git"

inherit pkgconfig gettext update-rc.d

DEPENDS = "pkgconfig fontconfig freetype libcap libjpeg-turbo"

RDEPENDS:${PN} = "glibc-gconv-iso8859-1"

RRECOMMENDS:${PN} = "glibc-gconv-iso8859-2 \
  glibc-gconv-iso8859-3 \
  glibc-gconv-iso8859-4 \
  glibc-gconv-iso8859-5 \
  glibc-gconv-iso8859-6 \
  glibc-gconv-iso8859-7 \
  glibc-gconv-iso8859-8 \
  glibc-gconv-iso8859-9 \
  glibc-gconv-iso8859-10 \
  glibc-gconv-iso8859-11 \
  glibc-gconv-iso8859-13 \
  glibc-gconv-iso8859-14 \
  glibc-gconv-iso8859-15 \
  glibc-gconv-iso-6937 \
  glibc-gconv-utf-16 \
  glibc-gconv-euc-kr \
  glibc-gconv-gbk \
"

EXTRA_OEMAKE="PREFIX=${prefix} \
  BINDIR=${bindir} \
  MANDIR=${mandir} \
  VIDEODIR=/hdd/vdr/video \
  CONFDIR=${localstatedir}/lib/vdr \
  ARGSDIR=${sysconfdir}/vdr/conf.d \
  CACHEDIR=${localstatedir}/cache/vdr \
  RESDIR=${datadir}/vdr \
  LIBDIR=${libdir}/vdr/plugins \
  LOCDIR=${datadir}/locale \
"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc ${PN}-dev"

FILES:${PN} =+ "${datadir}/locale"

CONFFILES:${PN} = "${sysconfdir}/default/vdr \
  ${sysconfdir}/vdr/conf.d/00-vdr.conf \
  ${sysconfdir}/vdr/diseqc.conf \
  ${sysconfdir}/vdr/keymacros.conf \
  ${sysconfdir}/vdr/scr.conf \
  ${sysconfdir}/vdr/sources.conf \
  ${sysconfdir}/vdr/svdrphosts.conf \
  ${sysconfdir}/vdr/camresponses.conf \
  ${sysconfdir}/vdr/fbc.conf \
  ${sysconfdir}/vdr/recording-hooks/R90.custom \
  ${sysconfdir}/vdr/shutdown-hooks/S90.custom \
  ${sysconfdir}/vdr/command-hooks/commands.custom.conf \
  ${sysconfdir}/vdr/command-hooks/reccmds.custom.conf \
"

INSANE_SKIP += "pkgconfig"

INITSCRIPT_NAME = "vdr"
INITSCRIPT_PARAMS = "start 80 5 . stop 80 0 6 1 3 ."

do_compile () {
  oe_runmake vdr i18n vdr.pc include-dir
  oe_runmake -C ${S}/PLUGINS/src/skincurses VDRDIR=${S} INCLUDES="-I${S}/include"
}

do_install () {
  oe_runmake install-i18n DESTDIR=${D}

  install -D -m 0755 -t ${D}${bindir} ${S}/vdr

  install -D -m 0755 ${WORKDIR}/vdr.sysvinit ${D}${sysconfdir}/init.d/vdr

  install -D -m 0644 -t ${D}${sysconfdir}/vdr ${S}/diseqc.conf
  install -D -m 0644 -t ${D}${sysconfdir}/vdr ${S}/keymacros.conf
  install -D -m 0644 -t ${D}${sysconfdir}/vdr ${S}/scr.conf
  install -D -m 0644 -t ${D}${sysconfdir}/vdr ${S}/sources.conf
  install -D -m 0644 -t ${D}${sysconfdir}/vdr ${S}/svdrphosts.conf
  install -D -m 0644 -t ${D}${sysconfdir}/vdr ${S}/camresponses.conf

  install -D -m 0644 -t ${D}${sysconfdir}/vdr ${WORKDIR}/fbc.conf

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/00-vdr.conf

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/recording-hooks ${WORKDIR}/R90.custom

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/shutdown-hooks ${WORKDIR}/S90.custom

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/command-hooks ${WORKDIR}/commands.custom.conf
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/command-hooks ${WORKDIR}/reccmds.custom.conf

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/channels.conf-examples ${S}/channels.conf*

  ln -s ${localstatedir}/lib/vdr/setup.conf ${D}${sysconfdir}/vdr
  ln -s ${localstatedir}/lib/vdr/remote.conf ${D}${sysconfdir}/vdr
  ln -s ${localstatedir}/lib/vdr/channels.conf ${D}${sysconfdir}/vdr
  ln -s ${sysconfdir}/default/vdr ${D}${sysconfdir}/vdr/vdr.default

  install -D -m 0644 ${WORKDIR}/vdr.default ${D}${sysconfdir}/default/vdr
  install -D -m 0644 ${WORKDIR}/vdr.volatiles ${D}${sysconfdir}/default/volatiles/50_vdr

  install -D -m 0644 -t ${D}${mandir}/man1 ${S}/vdr.1
  install -D -m 0644 -t ${D}${mandir}/man5 ${S}/vdr.5

  install -D -m 0644 -t ${D}${docdir}/vdr ${S}/README*
  install -D -m 0644 -t ${D}${docdir}/vdr ${S}/CONTRIBUTORS
  install -D -m 0644 -t ${D}${docdir}/vdr ${S}/MANUAL
  install -D -m 0644 -t ${D}${docdir}/vdr ${S}/INSTALL
  install -D -m 0644 -t ${D}${docdir}/vdr ${S}/UPDATE*
  install -D -m 0644 -t ${D}${docdir}/vdr ${WORKDIR}/README*

  install -D -m 0644 -t ${D}${libdir}/pkgconfig ${S}/vdr.pc
  install -D -m 0644 -t ${D}${includedir}/vdr ${S}/*.h
  install -D -m 0644 -t ${D}${includedir}/libsi ${S}/libsi/*.h

  install -D -m 0755 -t ${D}${libdir}/vdr ${WORKDIR}/runvdr
  install -D -m 0755 -t ${D}${libdir}/vdr ${WORKDIR}/vdr-recordingaction
  install -D -m 0755 -t ${D}${libdir}/vdr ${WORKDIR}/vdr-shutdown
  install -D -m 0755 -t ${D}${libdir}/vdr ${WORKDIR}/fbc-config
  install -D -m 0644 -t ${D}${libdir}/vdr ${WORKDIR}/config-loader.sh
  install -D -m 0644 -t ${D}${libdir}/vdr ${WORKDIR}/merge-commands.sh
  install -D -m 0644 -t ${D}${libdir}/vdr ${WORKDIR}/commands-loader.sh

  install -D -m 0644 -t ${D}${localstatedir}/lib/vdr/themes ${WORKDIR}/themes/sttng-blue.theme
  install -D -m 0644 -t ${D}${localstatedir}/lib/vdr/themes ${WORKDIR}/themes/sttng-cool.theme

  ln -s ${sysconfdir}/vdr/diseqc.conf ${D}${localstatedir}/lib/vdr
  ln -s ${sysconfdir}/vdr/keymacros.conf ${D}${localstatedir}/lib/vdr
  ln -s ${sysconfdir}/vdr/scr.conf ${D}${localstatedir}/lib/vdr
  ln -s ${sysconfdir}/vdr/sources.conf ${D}${localstatedir}/lib/vdr
  ln -s ${sysconfdir}/vdr/svdrphosts.conf ${D}${localstatedir}/lib/vdr
  ln -s ${sysconfdir}/vdr/camresponses.conf ${D}${localstatedir}/lib/vdr

  ln -s ${localstatedir}/cache/vdr/commands.conf ${D}${localstatedir}/lib/vdr
  ln -s ${localstatedir}/cache/vdr/reccmds.conf ${D}${localstatedir}/lib/vdr

  install -d -m 0755 ${D}${datadir}/vdr/command-hooks
  ln -s ${sysconfdir}/vdr/command-hooks/commands.custom.conf ${D}${datadir}/vdr/command-hooks
  ln -s ${sysconfdir}/vdr/command-hooks/reccmds.custom.conf ${D}${datadir}/vdr/command-hooks

  install -d -m 0755 ${D}${datadir}/vdr/recording-hooks
  ln -s ${sysconfdir}/vdr/recording-hooks/R90.custom ${D}${datadir}/vdr/recording-hooks

  install -d -m 0755 ${D}${datadir}/vdr/shutdown-hooks
  ln -s ${sysconfdir}/vdr/shutdown-hooks/S90.custom ${D}${datadir}/vdr/shutdown-hooks

  oe_runmake -C ${S}/PLUGINS/src/skincurses install VDRDIR=${S} DESTDIR=${D}
}

pkg_postinst:${PN} () {
  mkdir -p -m 0755 ${localstatedir}/cache/vdr
  mkdir -p -m 0755 /hdd/vdr/video
  if [ -x /etc/init.d/populate-volatile.sh ] ; then
    /etc/init.d/populate-volatile.sh update
  fi
}
