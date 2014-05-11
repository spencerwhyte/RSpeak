//
//  DeviceInformationDelegate.h
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-05-11.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DeviceInformation.h"

@protocol DeviceInformationRequestDelegate <NSObject>

-(void)deviceInformationRequestDidFail;
-(void)deviceInformationRequestDidSucceedWithDeviceInformation:(DeviceInformation *)deviceInformation;

@end
